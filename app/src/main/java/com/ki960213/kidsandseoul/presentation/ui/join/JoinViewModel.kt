package com.ki960213.kidsandseoul.presentation.ui.join

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.administrativedong.model.Borough
import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.auth.JoinResult
import com.ki960213.domain.auth.model.Authentication
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val administrativeDongRepository: AdministrativeDongRepository,
) : BaseViewModel() {

    private val _uiEvent: MutableSharedFlow<JoinUiEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<JoinUiEvent> = _uiEvent.asSharedFlow()

    private val authentication: Authentication = savedStateHandle
        .get<AuthenticationArgs>(JoinFragment.KEY_AUTHENTICATION)
        ?.asAuthentication()
        ?: throw IllegalArgumentException("회원가입 화면으로 이동할 때 AuthenticationArgs 인자를 넘기지 않았습니다.")

    val name = MutableStateFlow("")

    private val _administrativeDongs: MutableStateFlow<Map<Borough, List<AdministrativeDong>>> =
        MutableStateFlow(emptyMap())

    private val _boroughs: StateFlow<List<Borough>> = _administrativeDongs.map { it.keys.toList() }
        .viewModelStateIn(initialValue = emptyList())
    private val selectedBorough: MutableStateFlow<Borough?> = MutableStateFlow(null)
    val boroughs: StateFlow<List<BoroughUiState>> = combine(
        _boroughs, selectedBorough,
    ) { boroughs, selectedBorough ->
        boroughs.map {
            BoroughUiState(
                isSelected = it.id == selectedBorough?.id,
                borough = it
            )
        }
    }.viewModelStateIn(initialValue = emptyList())

    private val currentAdministrativeDongs: StateFlow<List<AdministrativeDong>> = combine(
        _administrativeDongs, selectedBorough
    ) { dongs, selectedBorough ->
        dongs[selectedBorough] ?: emptyList()
    }.viewModelStateIn(initialValue = emptyList())
    val selectedAdministrativeDong: MutableStateFlow<AdministrativeDong?> =
        MutableStateFlow(null)
    val administrativeDongs: StateFlow<List<AdministrativeDongUiState>> = combine(
        currentAdministrativeDongs, selectedAdministrativeDong
    ) { dongs, selectedDongs ->
        dongs.map {
            AdministrativeDongUiState(
                isSelected = it.id == selectedDongs?.id,
                administrativeDong = it,
            )
        }
    }.viewModelStateIn(initialValue = emptyList())

    val isJoinFormComplete: StateFlow<Boolean> = combine(
        name, selectedAdministrativeDong,
    ) { name, selectedAdministrativeDong ->
        name.isNotBlank() && selectedAdministrativeDong != null
    }.viewModelStateIn(initialValue = false)

    private val _isJoinWaiting = MutableStateFlow(false)
    val isJoinWaiting: StateFlow<Boolean> = _isJoinWaiting.asStateFlow()

    private val _isJustJoinSuccess = MutableStateFlow(false)
    val isJustJoinSuccess: StateFlow<Boolean> = _isJustJoinSuccess.asStateFlow()

    private val joinHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch { _uiEvent.emit(JoinUiEvent.JoinFail) }
    }

    init {
        fetchAdministrativeDongs()
    }

    private fun fetchAdministrativeDongs() = viewModelScope.launch {
        _administrativeDongs.value = administrativeDongRepository.getAdministrativeDongs()
            .let { it.values.groupBy { dong -> dong.borough } }
    }

    fun selectBorough(boroughId: Long) {
        selectedBorough.value = _boroughs.value.first { it.id == boroughId }
    }

    fun selectAdministrativeDong(administrativeDongId: Long) {
        selectedAdministrativeDong.value =
            currentAdministrativeDongs.value.firstOrNull { it.id == administrativeDongId }
    }

    fun join() = viewModelScope.launch(joinHandler) {
        _isJoinWaiting.value = true
        val joinResult =
            authRepository.join(authentication, name.value, selectedAdministrativeDong.value!!.id)
        when (joinResult) {
            JoinResult.Fail -> _uiEvent.emit(JoinUiEvent.JoinFail)
            JoinResult.Success -> {
                _isJustJoinSuccess.value = true
                authRepository.login(authentication)
            }
        }
        _isJoinWaiting.value = false
    }
}
