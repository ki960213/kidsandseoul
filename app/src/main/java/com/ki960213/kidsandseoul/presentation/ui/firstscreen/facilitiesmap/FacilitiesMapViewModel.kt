package com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap

import androidx.lifecycle.viewModelScope
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap.uistate.FacilitiesMapUiEvent
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap.uistate.SearchConditionsUiState
import com.naver.maps.geometry.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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

    val keyword: MutableStateFlow<String> = MutableStateFlow("")

    val service: MutableStateFlow<ServiceUiState?> = MutableStateFlow(null)

    val latLngBounds: MutableStateFlow<LatLngBounds?> = MutableStateFlow(null)

    private val _searchConditions: MutableStateFlow<SearchConditionsUiState> = MutableStateFlow(SearchConditionsUiState())
    val searchConditions: StateFlow<SearchConditionsUiState> = _searchConditions.asStateFlow()

    val isReSearchMeaningful: StateFlow<Boolean> = combine(
        latLngBounds,
        searchConditions,
    ) { latLngBounds, searchConditions ->
        searchConditions.isValid && latLngBounds != searchConditions.latLngBounds && searchConditions.keyword.isBlank()
    }.viewModelStateIn(initialValue = false)

    val facilities: StateFlow<List<Facility>> = _searchConditions
        .map { searchConditions ->
            if (!searchConditions.isValid) return@map emptyList()
            _isLoading.value = true
            val conditions = searchConditions.asFacilityFilterConditions()
            facilityRepository.getFacilities(
                page = FIRST_PAGE,
                size = MAX_FACILITY_SIZE,
                conditions = conditions,
            ).also { if (it.isEmpty()) _uiEvent.emit(FacilitiesMapUiEvent.NotFoundFacilities) }
        }
        .onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = emptyList())

    private val _selectedFacility: MutableStateFlow<Facility?> = MutableStateFlow(null)
    val selectedFacility: StateFlow<Facility?> = _selectedFacility.asStateFlow()

    val canResetFilterConditions: StateFlow<Boolean> = searchConditions.map { it.isValid }
        .viewModelStateIn(initialValue = false)

    fun search() = viewModelScope.launch {
        val newSearchConditions = SearchConditionsUiState(
            keyword = keyword.value,
            service = service.value,
            latLngBounds = latLngBounds.value,
        )
        if (newSearchConditions.isNotValidBecauseTooShortKeyword) _uiEvent.emit(FacilitiesMapUiEvent.KeywordTooShort)
        _searchConditions.value = newSearchConditions
    }

    fun selectFacility(facilityId: Long?) {
        _selectedFacility.value = facilities.value.find { it.id == facilityId }
    }

    fun resetFilterConditions() {
        require(canResetFilterConditions.value)
        keyword.value = ""
        service.value = null
        _searchConditions.value = SearchConditionsUiState()
    }

    companion object {

        private const val FIRST_PAGE = 0
        private const val MAX_FACILITY_SIZE = 10000
    }
}
