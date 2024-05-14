package com.ki960213.kidsandseoul.presentation.ui.myinterestfacilities

import androidx.lifecycle.SavedStateHandle
import com.ki960213.domain.facility.model.Facility
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
        ?: throw IllegalArgumentException("내 관심 시설 화면으로 이동할 때 유저 아이디 안넘겼음")

    val interestFacilities: StateFlow<List<Facility>> = facilityRepository.getInterestFacilities(userId)
        .viewModelStateIn(initialValue = emptyList())
}
