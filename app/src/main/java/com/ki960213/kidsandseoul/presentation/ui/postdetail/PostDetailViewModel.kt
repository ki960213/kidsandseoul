package com.ki960213.kidsandseoul.presentation.ui.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.comment.repository.CommentRepository
import com.ki960213.domain.post.model.Post
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
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
) : BaseViewModel() {

    private val _uiEvent = MutableSharedFlow<PostDetailUiEvent>()
    val uiEvent: SharedFlow<PostDetailUiEvent> = _uiEvent.asSharedFlow()

    private val postId: String = savedStateHandle[PostDetailFragment.KEY_POST_ID]
        ?: throw IllegalArgumentException("자유게시글 아이디 안넘김")

    private val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    val isLogin: StateFlow<Boolean> = loginUserId.map { it != null }
        .viewModelStateIn(initialValue = false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = loginUserId.flatMapLatest {
        if (it == null) flowOf(null) else userRepository.getUser(userId = it)
    }
        .map { user ->
            when (user) {
                is JoinedUser -> user
                LeavedUser, null -> null
            }
        }
        .viewModelStateIn(initialValue = null)

    val loginUserIsAuthor: Boolean
        get() {
            val authorId = items.value.filterIsInstance<PostDetailUiState>()
                .firstOrNull()
                ?.post
                ?.authorId
            return loginUserId.value == authorId
        }

    val items: StateFlow<List<PostDetailItemUiState>> = combine(
        loginUser,
        postRepository.getPost(postId),
        commentRepository.getCommentsOfPost(postId),
    ) { loginUser, post, comments ->
        if (post !is Post) {
            _uiEvent.emit(PostDetailUiEvent.DeletedPostFetch)
            return@combine emptyList()
        }
        val authorIds = (comments.map { it.authorId } + post.authorId).distinct()
        val users = userRepository.getUsers(authorIds)
            .filterIsInstance<JoinedUser>()
            .associateBy { it.id }

        val postUiState = PostDetailUiState(
            post = post,
            author = users[post.authorId] ?: return@combine emptyList(),
            isLike = post.id in (loginUser?.likePostIds ?: emptyList()),
        )

        val parentComments = comments.filter { !it.isChildComment }.sortedBy { it.createdAt }
        val sortedComments = parentComments.map { comment ->
            val childComments = comments.filter { it.parentCommentId == comment.id }
            listOf(comment) + childComments
        }.flatten()

        val commentsUiState = sortedComments.mapNotNull { comment ->
            CommentUiState(
                comment = comment,
                author = users[comment.authorId] ?: return@mapNotNull null,
                isDeletable = comment.authorId == loginUser?.id,
            )
        }

        listOf(postUiState) + commentsUiState
    }.viewModelStateIn(initialValue = emptyList())

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
