package com.ki960213.kidsandseoul.presentation.ui.following

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.followers.users.FollowUserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val userId: String = savedStateHandle[FollowingsFragment.KEY_USER_ID]
        ?: throw AssertionError("유저 아이디를 안넘겼음.")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val loginUser: StateFlow<JoinedUser?> = authRepository.loginUserId
        .filterNotNull()
        .flatMapLatest { loginUserId ->
            userRepository.getUser(loginUserId)
        }.filterIsInstance<JoinedUser>()
        .viewModelStateIn(initialValue = null)

    val followingUsers: StateFlow<List<FollowUserUiState>> = combine(
        loginUser,
        userRepository.getFollowingUsers(userId)
    ) { loginUser, users ->
        users.map { user ->
            FollowUserUiState(
                isFollowing = user.id in (loginUser?.followingIds ?: emptySet()),
                isMe = loginUser?.id == user.id,
                user = user,
            )
        }
    }.viewModelStateIn(initialValue = emptyList())

    fun setFollow(userId: String, willFollow: Boolean) = viewModelScope.launch {
        val loginUser = loginUser.value ?: return@launch
        if (willFollow) {
            userRepository.follow(loginUser.id, userId)
        } else {
            userRepository.unfollow(loginUser.id, userId)
        }
    }
}
