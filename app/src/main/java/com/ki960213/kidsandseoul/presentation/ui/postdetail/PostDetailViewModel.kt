package com.ki960213.kidsandseoul.presentation.ui.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.comment.model.Comment
import com.ki960213.domain.comment.repository.CommentRepository
import com.ki960213.domain.post.repository.PostRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.CommentUiState
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailItemUiState
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
) : BaseViewModel() {

    private val _uiEvent = MutableSharedFlow<PostDetailUiEvent>()
    val uiEvent: SharedFlow<PostDetailUiEvent> = _uiEvent.asSharedFlow()

    private val postId: String = savedStateHandle[PostDetailFragment.KEY_POST_ID]
        ?: throw IllegalArgumentException("게시글 상세 화면으로 이동할 때 게시글 아이디 안넘김")

    private val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    val isLogin: StateFlow<Boolean> = loginUserId.map { it != null }
        .viewModelStateIn(initialValue = false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = loginUserId.flatMapLatest { loginUserId ->
        if (loginUserId == null) flowOf(null) else userRepository.getUser(userId = loginUserId)
    }.map { user ->
        when (user) {
            is JoinedUser -> user
            LeavedUser, null -> null
        }
    }.viewModelStateIn(initialValue = null)

    val loginUserIsAuthor: Boolean
        get() {
            val authorId = items.value.filterIsInstance<PostDetailUiState>()
                .firstOrNull()
                ?.post
                ?.authorId
            return loginUserId.value == authorId
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: StateFlow<List<PostDetailItemUiState>> = combine(
        postRepository.getPost(postId),
        commentRepository.getCommentsOfPost(postId),
    ) { post, comments ->
        post to comments
    }.flatMapLatest { (post, comments) ->
        if (post == null) {
            _uiEvent.emit(PostDetailUiEvent.DeletedPostFetch)
            return@flatMapLatest flowOf(emptyList())
        }
        val authorIds = (comments.map { it.authorId } + post.authorId).distinct()
        combine(
            userRepository.getUsers(authorIds),
            loginUser,
        ) { users, loginUser ->
            val authors = users.filterIsInstance<JoinedUser>()
                .associateBy { it.id }

            val loginUserLikeThisPost = loginUser?.likePostIds?.contains(post.id) ?: false
            val postUiState = PostDetailUiState(
                post = post,
                author = authors[post.authorId] ?: LeavedUser,
                isLike = loginUserLikeThisPost,
            )

            val sortedComments = comments.sortedWithChildComments()
            val commentsUiState = sortedComments.toCommentUiStates(authors, loginUser)
            listOf(postUiState) + commentsUiState
        }
    }.viewModelStateIn(initialValue = emptyList())

    private fun List<Comment>.sortedWithChildComments(): List<Comment> {
        val parentComments = filter { it.isParentComment }
            .sortedBy { it.createdAt }
        return parentComments.map { parentComment ->
            val childComments = filter { it.isChildCommentOf(parentComment) }
            listOf(parentComment) + childComments
        }.flatten()
    }

    private fun List<Comment>.toCommentUiStates(
        authors: Map<String, JoinedUser>,
        loginUser: JoinedUser?,
    ): List<CommentUiState> = mapNotNull { comment ->
        val loginUserIsAuthor = comment.authorId == loginUser?.id
        CommentUiState(
            comment = comment,
            author = authors[comment.authorId] ?: LeavedUser,
            isDeletable = loginUserIsAuthor,
        )
    }

    fun setLike(postId: String, isLike: Boolean) = viewModelScope.launch {
        val loginUserId = loginUserId.value ?: return@launch
        if (isLike) {
            postRepository.like(loginUserId, postId)
        } else {
            postRepository.unlike(loginUserId, postId)
        }
    }

    fun sendComment(content: String) = viewModelScope.launch {
        commentRepository.writeComment(
            authorId = loginUserId.value ?: return@launch,
            postId = postId,
            content = content,
        )
    }

    fun deletePost() = viewModelScope.launch {
        postRepository.deletePost(postId)
    }

    fun deleteComment(commentId: String) = viewModelScope.launch {
        commentRepository.deleteComment(postId, commentId)
    }
}
