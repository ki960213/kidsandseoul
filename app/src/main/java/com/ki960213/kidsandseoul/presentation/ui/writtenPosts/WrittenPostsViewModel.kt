package com.ki960213.kidsandseoul.presentation.ui.writtenPosts

import androidx.lifecycle.SavedStateHandle
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.post.repository.PostRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WrittenPostsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : BaseViewModel() {

    private val authorId: String = savedStateHandle[WrittenPostsFragment.KEY_WRITTEN_USER_ID]
        ?: throw IllegalArgumentException("작성한 게시물 화면으로 이동할 때 유저 아이디 안넘겼음")

    val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = loginUserId.flatMapLatest { loginUserId ->
        if (loginUserId == null) flowOf(null) else userRepository.getUser(userId = loginUserId)
    }
        .map { user ->
            when (user) {
                is JoinedUser -> user
                LeavedUser, null -> null
            }
        }
        .viewModelStateIn(initialValue = null)

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val posts: StateFlow<List<PostUiState>> = combine(
        loginUser,
        userRepository.getUser(authorId).filterIsInstance<JoinedUser>(),
        postRepository.getPostsOfUser(authorId),
    ) { loginUser, author, posts ->
        posts.map { post ->
            val loginUserLikeThisPost = loginUser?.likePostIds?.contains(post.id) ?: false
            PostUiState(
                post = post,
                author = author,
                isLike = loginUserLikeThisPost,
            )
        }
    }.onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = emptyList())
}
