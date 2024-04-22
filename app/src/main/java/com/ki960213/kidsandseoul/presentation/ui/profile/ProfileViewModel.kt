package com.ki960213.kidsandseoul.presentation.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.kid.repository.KidRepository
import com.ki960213.domain.post.repository.PostRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts.PostUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids.KidUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids.KidsItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val kidRepository: KidRepository,
    private val postRepository: PostRepository,
) : BaseViewModel() {

    val userId: String = savedStateHandle[ProfileFragment.KEY_USER_ID]
        ?: throw AssertionError("유저 아이디 인자를 안넘겼음.")

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val user: StateFlow<JoinedUser?> = userRepository.getUser(userId)
        .onEach { if (it is LeavedUser) _uiEvent.emit(ProfileUiEvent.LeavedUserFetch) }
        .filterIsInstance<JoinedUser>()
        .onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = authRepository.loginUserId.flatMapLatest {
        if (it == null) flowOf(null) else userRepository.getUser(userId = it)
    }
        .map { user ->
            when (user) {
                is JoinedUser -> user
                LeavedUser, null -> null
            }
        }
        .viewModelStateIn(initialValue = null)

    val isFollowing: StateFlow<Boolean> = user.filterNotNull()
        .zip(loginUser.filterNotNull()) { user, loginUser ->
            user.id in loginUser.followingIds
        }.viewModelStateIn(initialValue = false)

    val kids: StateFlow<List<KidsItemUiState>> = kidRepository.getKids(userId).map { kids ->
        kids.kids.map { kid ->
            KidUiState(kids.parentId, kid)
        }
    }.viewModelStateIn(initialValue = emptyList())

    val recentPost: StateFlow<PostUiState?> = combine(
        loginUser,
        user.filterNotNull(),
        postRepository.getPostsOfUser(userId),
    ) { loginUser, user, posts ->
        val recentPost = posts.maxByOrNull { it.createdAt } ?: return@combine null
        when (loginUser) {
            is JoinedUser -> PostUiState(
                post = recentPost,
                author = user,
                isLike = recentPost.id in loginUser.likePostIds
            )

            else -> PostUiState(
                post = recentPost,
                author = user,
                isLike = false
            )
        }
    }.viewModelStateIn(initialValue = null)

    private val _uiEvent = MutableSharedFlow<ProfileUiEvent>()
    val uiEvent: SharedFlow<ProfileUiEvent> = _uiEvent.asSharedFlow()

    fun setFollow(isFollow: Boolean) = viewModelScope.launch {
        val loginUserId = loginUser.value?.id ?: return@launch
        if (isFollow) {
            userRepository.follow(loginUserId, targetUserId = userId)
        } else {
            userRepository.unfollow(loginUserId, targetUserId = userId)
        }
    }
}
