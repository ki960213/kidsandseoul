package com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile

import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.kid.repository.KidRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids.KidAddUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids.KidUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids.KidsItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val kidRepository: KidRepository,
) : BaseViewModel() {

    private val loginUserId: Flow<String?> = authRepository.loginUserId

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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
        .onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = null)

    val isLogin: StateFlow<Boolean> = loginUser.map { it != null }
        .viewModelStateIn(initialValue = false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val kids: StateFlow<List<KidsItemUiState>> = loginUserId
        .flatMapLatest { userId ->
            if (userId == null) return@flatMapLatest flowOf(null)
            kidRepository.getKids(userId)
        }.map { kids ->
            (kids?.kids?.map { kid ->
                KidUiState(kids.parentId, kid)
            } ?: emptyList<KidAddUiState>()) + KidAddUiState
        }.viewModelStateIn(initialValue = emptyList())

    fun logout() = viewModelScope.launch {
        authRepository.logout()
    }

    fun leave() = viewModelScope.launch {
        _isLoading.value = true
        userRepository.leave(loginUser.value?.id ?: return@launch)
        _isLoading.value = false
    }
}
