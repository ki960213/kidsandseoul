package com.ki960213.kidsandseoul.presentation.ui.myinterestfacilities

import androidx.lifecycle.SavedStateHandle
import com.ki960213.domain.facility.model.Facilities
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyInterestFacilitiesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    facilityRepository: FacilityRepository,
) : BaseViewModel() {

    private val userId: String = savedStateHandle[MyInterestFacilitiesFragment.KEY_USER_ID]
        ?: throw IllegalArgumentException()

    val interestFacilities: StateFlow<Facilities> = facilityRepository.getInterestFacilities(userId)
        .viewModelStateIn(initialValue = Facilities(allCount = 0, value = emptyList()))
}
