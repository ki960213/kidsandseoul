package com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap

import androidx.lifecycle.viewModelScope
import com.ki960213.domain.facility.model.ChildCareFacilityFilterCondition
import com.ki960213.domain.facility.model.ChildCareService
import com.ki960213.domain.facility.model.Facilities
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.FacilityFilterCondition
import com.ki960213.domain.facility.model.FacilityType
import com.ki960213.domain.facility.model.KidsCafeFilterCondition
import com.ki960213.domain.facility.model.OtherFacilityFilterCondition
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.CO_PARENTING_ROOM
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.CO_PARENTING_SHARING_CENTER
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.EXPERIENCE
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.KIDS_CAFE
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.LIBRARY
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.LOCAL_CHILDREN_CENTER
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.MEDICAL
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.OUR_NEIGHBORHOOD_GROWING_CENTER
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState.OUTDOOR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacilitiesMapViewModel @Inject constructor(
    private val facilityRepository: FacilityRepository,
) : BaseViewModel() {

    private val _uiEvent = MutableSharedFlow<FacilitiesMapUiEvent>()
    val uiEvent: SharedFlow<FacilitiesMapUiEvent> = _uiEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword.asStateFlow()

    private val _selectedService: MutableStateFlow<ServiceUiState?> = MutableStateFlow(null)
    val selectedService: StateFlow<ServiceUiState?> = _selectedService.asStateFlow()

    val facilities: StateFlow<Facilities> = combine(
        _searchKeyword,
        _selectedService,
    ) { keyword, service ->
        _isLoading.value = true
        if (keyword.isBlank() && service == null) {
            return@combine Facilities(allCount = 0, value = emptyList())
        }

        val condition = when (service) {
            OUR_NEIGHBORHOOD_GROWING_CENTER -> ChildCareFacilityFilterCondition(
                name = keyword,
                childCareService = ChildCareService.OUR_NEIGHBORHOOD_GROWING_CENTER
            )

            CO_PARENTING_ROOM -> ChildCareFacilityFilterCondition(
                name = keyword,
                childCareService = ChildCareService.CO_PARENTING_ROOM
            )

            LOCAL_CHILDREN_CENTER -> ChildCareFacilityFilterCondition(
                name = keyword,
                childCareService = ChildCareService.LOCAL_CHILDREN_CENTER
            )

            CO_PARENTING_SHARING_CENTER -> ChildCareFacilityFilterCondition(
                name = keyword,
                childCareService = ChildCareService.CO_PARENTING_SHARING_CENTER
            )

            KIDS_CAFE -> KidsCafeFilterCondition(
                name = keyword,
            )

            OUTDOOR -> OtherFacilityFilterCondition(
                name = keyword,
                facilityType = FacilityType.OUTDOOR,
            )

            EXPERIENCE -> OtherFacilityFilterCondition(
                name = keyword,
                facilityType = FacilityType.EXPERIENCE,
            )

            MEDICAL -> OtherFacilityFilterCondition(
                name = keyword,
                facilityType = FacilityType.MEDICAL,
            )

            LIBRARY -> OtherFacilityFilterCondition(
                name = keyword,
                facilityType = FacilityType.LIBRARY,
            )

            null -> FacilityFilterCondition(name = keyword)
        }
        facilityRepository.getFacilities(condition)
    }.onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = Facilities(allCount = 0, value = emptyList()))

    private val _selectedFacility: MutableStateFlow<Facility?> = MutableStateFlow(null)
    val selectedFacility: StateFlow<Facility?> = _selectedFacility.asStateFlow()

    fun setKeyword(keyword: String) = viewModelScope.launch {
        if ((_searchKeyword.value.isNotBlank() && keyword.isEmpty()) || keyword.length >= 2) {
            _searchKeyword.value = keyword
            return@launch
        }
        _uiEvent.emit(FacilitiesMapUiEvent.KeywordTooShort)
    }

    fun setService(serviceUiState: ServiceUiState) {
        _selectedService.value = serviceUiState
    }

    fun selectFacility(facilityId: Long?) {
        val facility = facilities.value.value.find { it.id == facilityId }
        _selectedFacility.value = facility
    }

    fun resetFilterCondition() {
        _searchKeyword.value = ""
        _selectedService.value = null
        _selectedFacility.value = null
    }
}
