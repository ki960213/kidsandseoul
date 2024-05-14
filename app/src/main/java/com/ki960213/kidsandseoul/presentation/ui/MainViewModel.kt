package com.ki960213.kidsandseoul.presentation.ui

import androidx.lifecycle.viewModelScope
import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.auth.model.Authentication
import com.ki960213.domain.auth.model.LoginResult
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.kidsandseoul.presentation.common.EventFlow
import com.ki960213.kidsandseoul.presentation.common.MutableEventFlow
import com.ki960213.kidsandseoul.presentation.common.asEventFlow
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _uiEvent: MutableEventFlow<MainUiEvent> = MutableEventFlow()
    val uiEvent: EventFlow<MainUiEvent> = _uiEvent.asEventFlow()

    private val _isLoginWaiting = MutableStateFlow(false)
    val isLoginWaiting: StateFlow<Boolean> = _isLoginWaiting.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.loginUserId.collect {
                val isNotLogin = it == null
                if (isNotLogin) _uiEvent.emit(MainUiEvent.ShowLoginDialog)
                cancel()
            }
        }
    }

    fun login(authentication: Authentication) = viewModelScope.launch {
        _isLoginWaiting.value = true
        val loginResult = authRepository.login(authentication)
        when (loginResult) {
            LoginResult.LoginFail -> _uiEvent.emit(MainUiEvent.LoginFail)
            LoginResult.LoginSuccess -> {}
            LoginResult.NotJoinedUser -> _uiEvent.emit(MainUiEvent.NavigateToJoin(authentication))
        }
        _isLoginWaiting.value = false
    }

    fun dispatchLoginEvent() = viewModelScope.launch {
        _uiEvent.emit(MainUiEvent.ShowLoginDialog)
    }
}
