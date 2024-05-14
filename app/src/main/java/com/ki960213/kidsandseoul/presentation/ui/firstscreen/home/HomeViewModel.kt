package com.ki960213.kidsandseoul.presentation.ui.firstscreen.home

import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val facilityRepository: FacilityRepository,
) : BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val interestFacilities: StateFlow<List<Facility>> = authRepository.loginUserId
        .flatMapLatest { loginUserId ->
            if (loginUserId == null) {
                return@flatMapLatest flowOf(emptyList())
            }
            facilityRepository.getInterestFacilities(loginUserId)
        }
        .viewModelStateIn(initialValue = emptyList())
}
