package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.administrativedong.model.Borough
import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.facility.model.ChildCareService
import com.ki960213.domain.facility.model.Facilities
import com.ki960213.domain.facility.model.FacilityType
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.domain.kid.model.Kid
import com.ki960213.domain.kid.repository.KidRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class FacilitySearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val facilityRepository: FacilityRepository,
    private val administrativeDongRepository: AdministrativeDongRepository,
    private val authRepository: AuthRepository,
    private val kidRepository: KidRepository,
) : BaseViewModel() {

    private val _uiEvent = MutableSharedFlow<FacilitiesUiEvent>()
    val uiEvent: SharedFlow<FacilitiesUiEvent> = _uiEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val facilitySearchArg: StateFlow<FacilitySearchArg> = savedStateHandle.getStateFlow(
        FacilitySearchFragment.KEY_FACILITY_SEARCH_ARG,
        FacilitySearchArg.Keyword("")
    )

    private val keywordArg: MutableStateFlow<String?> = facilitySearchArg
        .filterIsInstance<FacilitySearchArg.Keyword>()
        .map { it.keyword }
        .viewModelMutableStateIn(initialValue = "")

    val serviceArg: StateFlow<ServiceUiState?> = facilitySearchArg
        .filterIsInstance<FacilitySearchArg.Service>()
        .map { it.type }
        .viewModelStateIn(initialValue = null)

    private val _detailCondition: MutableStateFlow<DetailFilterConditionUiState?> = serviceArg
        .map { serviceUiState -> serviceUiState?.toDetailFilterCondition() }
        .viewModelMutableStateIn(initialValue = null)
    val detailCondition: StateFlow<DetailFilterConditionUiState?> = _detailCondition.asStateFlow()

    val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val kids: StateFlow<List<Kid>> = loginUserId.flatMapLatest { loginUserId ->
        if (loginUserId == null) {
            flowOf(emptyList())
        } else {
            kidRepository.getKids(loginUserId).map { it.kids }
        }
    }.viewModelStateIn(initialValue = emptyList())

    val age: MutableStateFlow<String> = MutableStateFlow("")
        .map { it.replace(NOT_NUMBER_REGEX, "") }
        .viewModelMutableStateIn(initialValue = "")

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

    val canFiltering: StateFlow<Boolean> = combine(
        _detailCondition, selectedAdministrativeDong
    ) { detailCondition, selectedAdministrativeDong ->
        if (detailCondition?.isComplete != true) return@combine false
        if (detailCondition is OtherDetailFilterConditionUiState) return@combine selectedAdministrativeDong != null
        true
    }.viewModelStateIn(initialValue = false)

    private val _appliedCondition: MutableStateFlow<FilterConditionUiState> =
        keywordArg.combine(serviceArg) { keyword, service ->
            keyword to service
        }.filter { (keyword, service) ->
            keyword?.isNotBlank() == true || service?.isNotOtherFacility == true
        }.map { (keyword, service) ->
            FilterConditionUiState(
                keyword = keyword,
                detailCondition = service?.toDetailFilterCondition()
            )
        }.viewModelMutableStateIn(initialValue = FilterConditionUiState())
    val appliedCondition: StateFlow<FilterConditionUiState> = _appliedCondition.asStateFlow()

    private val _isReviewCountDescending = MutableStateFlow(true)
    val isReviewCountDescending: StateFlow<Boolean> = _isReviewCountDescending.asStateFlow()

    private val _facilities: Flow<Facilities> = appliedCondition
        .filter { it.isNotEmpty }
        .map { condition ->
            _isLoading.value = true
            facilityRepository.getFacilities(condition.asFacilityFilterCondition())
        }.onEach { _isLoading.value = false }
    val facilities: StateFlow<Facilities> = combine(
        _facilities,
        isReviewCountDescending,
    ) { facilities, isDescending ->
        facilities.sortedByReviewCount(isDescending)
    }.viewModelStateIn(initialValue = Facilities(allCount = 0, value = emptyList()))

    init {
        fetchAdministrativeDongs()
    }

    private fun fetchAdministrativeDongs() = viewModelScope.launch {
        _administrativeDongs.value = administrativeDongRepository.getAdministrativeDongs()
            .let { it.values.groupBy { dong -> dong.borough } }
    }

    private fun ServiceUiState.toDetailFilterCondition(): DetailFilterConditionUiState =
        when (this) {
            ServiceUiState.OUR_NEIGHBORHOOD_GROWING_CENTER -> ChildCareFilterConditionUiState(
                service = ChildCareService.OUR_NEIGHBORHOOD_GROWING_CENTER,
            )

            ServiceUiState.CO_PARENTING_ROOM -> ChildCareFilterConditionUiState(
                service = ChildCareService.CO_PARENTING_ROOM,
            )

            ServiceUiState.LOCAL_CHILDREN_CENTER -> ChildCareFilterConditionUiState(
                service = ChildCareService.LOCAL_CHILDREN_CENTER,
            )

            ServiceUiState.CO_PARENTING_SHARING_CENTER -> ChildCareFilterConditionUiState(
                service = ChildCareService.CO_PARENTING_SHARING_CENTER,
            )

            ServiceUiState.KIDS_CAFE -> KidsCafeConditionUiState()
            ServiceUiState.OUTDOOR -> OtherDetailFilterConditionUiState(
                type = FacilityType.OUTDOOR,
            )

            ServiceUiState.EXPERIENCE -> OtherDetailFilterConditionUiState(
                type = FacilityType.EXPERIENCE
            )

            ServiceUiState.MEDICAL -> OtherDetailFilterConditionUiState(
                type = FacilityType.MEDICAL
            )

            ServiceUiState.LIBRARY -> OtherDetailFilterConditionUiState(
                type = FacilityType.LIBRARY
            )
        }

    fun setIsDescending(isDescending: Boolean) {
        _isReviewCountDescending.value = isDescending
    }

    fun setKeyword(keyword: String) = viewModelScope.launch {
        val prevKeyword = _appliedCondition.value.keyword ?: return@launch
        if ((prevKeyword.isNotBlank() && keyword.isEmpty()) || keyword.length >= 2) {
            _appliedCondition.value = _appliedCondition.value.copy(keyword = keyword)
            return@launch
        }
        _uiEvent.emit(FacilitiesUiEvent.KeywordTooShort)
    }

    fun setChildCareDetailCondition() {
        if (_detailCondition.value is ChildCareFilterConditionUiState) return
        _detailCondition.value = ChildCareFilterConditionUiState()
    }

    fun setKidsCafeDetailCondition() {
        if (_detailCondition.value is KidsCafeConditionUiState) return
        _detailCondition.value = KidsCafeConditionUiState()
    }

    fun setOtherDetailCondition() {
        if (_detailCondition.value is OtherDetailFilterConditionUiState) return
        _detailCondition.value = OtherDetailFilterConditionUiState()
    }

    fun setChildCareService(childCareService: ChildCareService) {
        val detailCondition = _detailCondition.value
        if (detailCondition !is ChildCareFilterConditionUiState) return
        _detailCondition.value = detailCondition.copy(service = childCareService)
    }

    fun setMustOperateSaturday(value: Boolean) {
        val detailCondition = _detailCondition.value
        if (detailCondition !is ChildCareFilterConditionUiState) return
        _detailCondition.value = detailCondition.copy(mustSaturdayOperate = value)
    }

    fun toggleDayOfWeek(dayOfWeek: DayOfWeek) {
        val detailCondition = _detailCondition.value
        if (detailCondition !is KidsCafeConditionUiState) return
        if (dayOfWeek in detailCondition.daysOfWeek) {
            _detailCondition.value =
                detailCondition.copy(daysOfWeek = detailCondition.daysOfWeek - dayOfWeek)
        } else {
            _detailCondition.value =
                detailCondition.copy(daysOfWeek = detailCondition.daysOfWeek + dayOfWeek)
        }
    }

    fun setFacilityType(facilityType: FacilityType) {
        val detailCondition = _detailCondition.value
        if (detailCondition !is OtherDetailFilterConditionUiState) return
        _detailCondition.value = detailCondition.copy(type = facilityType)
    }

    fun changeConditionToFitKid(kidId: String) {
        val kid = kids.value.find { it.id == kidId } ?: return
        age.value = kid.age.toString()
        selectedAdministrativeDong.value = kid.livingDong
    }

    fun selectBorough(boroughId: Long) {
        selectedBorough.value = _boroughs.value.firstOrNull { it.id == boroughId }
    }

    fun selectAdministrativeDong(administrativeDongId: Long) {
        selectedAdministrativeDong.value =
            currentAdministrativeDongs.value.firstOrNull { it.id == administrativeDongId }
    }

    fun resetFilterCondition() {
        _detailCondition.value = null
        age.value = ""
        selectedBorough.value = null
        selectedAdministrativeDong.value = null
    }

    fun applyFilterCondition() {
        _appliedCondition.value = _appliedCondition.value.copy(
            detailCondition = _detailCondition.value,
            age = age.value.toIntOrNull(),
            region = selectedAdministrativeDong.value
        )
    }

    companion object {

        private val NOT_NUMBER_REGEX = Regex("[^0-9]")
    }
}
