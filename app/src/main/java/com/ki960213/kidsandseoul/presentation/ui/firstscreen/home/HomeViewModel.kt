package com.ki960213.kidsandseoul.presentation.ui.firstscreen.home

import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.facility.model.Facilities
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
    private val authRepository: AuthRepository,
    private val facilityRepository: FacilityRepository,
) : BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val interestFacilities: StateFlow<Facilities> = authRepository.loginUserId
        .flatMapLatest { loginUserId ->
            if (loginUserId == null) {
                return@flatMapLatest flowOf(Facilities(allCount = 0, value = emptyList()))
            }
            facilityRepository.getInterestFacilities(loginUserId)
        }
        .viewModelStateIn(initialValue = Facilities(allCount = 0, value = emptyList()))
}
