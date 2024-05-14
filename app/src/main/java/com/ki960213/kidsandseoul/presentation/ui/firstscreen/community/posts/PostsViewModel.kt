package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts

import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.post.model.Post
import com.ki960213.domain.post.repository.PostRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    authRepository: AuthRepository,
    postRepository: PostRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = loginUserId.flatMapLatest { loginUserId ->
        if (loginUserId == null) flowOf(null) else userRepository.getUser(userId = loginUserId)
    }.map { user ->
        when (user) {
            is JoinedUser -> user
            LeavedUser, null -> null
        }
    }.viewModelStateIn(initialValue = null)

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: StateFlow<List<PostUiState>> = postRepository.getRecentPosts()
        .flatMapLatest { posts ->
            combine(
                userRepository.getAuthorsOf(posts),
                loginUser,
            ) { authors, loginUser ->
                posts.toPostUiStates(authors, loginUser)
            }
        }.onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = emptyList())

    private fun UserRepository.getAuthorsOf(posts: List<Post>): Flow<Map<String, JoinedUser>> {
        val authorIds = posts.map { it.authorId }.distinct()
        return getUsers(authorIds)
            .map { users ->
                users.filterIsInstance<JoinedUser>()
                    .associateBy { it.id }
            }
    }

    private fun List<Post>.toPostUiStates(
        authors: Map<String, JoinedUser>,
        loginUser: JoinedUser?,
    ): List<PostUiState> = mapNotNull { post ->
        val author = authors[post.authorId] ?: LeavedUser
        val loginUserLikeThisPost = loginUser?.likePostIds?.contains(post.id) ?: false
        PostUiState(
            post = post,
            author = author,
            isLike = loginUserLikeThisPost,
        )
    }
}
