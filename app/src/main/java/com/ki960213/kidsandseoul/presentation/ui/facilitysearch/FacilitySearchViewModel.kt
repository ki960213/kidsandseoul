package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.administrativedong.model.Borough
import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.facility.model.ChildCareFacilityType
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.FacilityOrder
import com.ki960213.domain.facility.model.OtherFacilityType
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.domain.kid.model.Kid
import com.ki960213.domain.kid.repository.KidRepository
import com.ki960213.kidsandseoul.data.pagesource.FacilitiesPagingSource
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.ChildCareFilterConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.DetailFilterConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.FilterConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.KidsCafeConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.OtherDetailFilterConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.RegionUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.SelectableRegionUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class FacilitySearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    administrativeDongRepository: AdministrativeDongRepository,
    private val facilityRepository: FacilityRepository,
    private val kidRepository: KidRepository,
) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val facilitySearchArg: FacilitySearchArg = savedStateHandle[FacilitySearchFragment.KEY_FACILITY_SEARCH_ARG]
        ?: throw IllegalArgumentException("시설 목록 화면으로 이동할 때 시설 필터 조건을 안넘겼음")

    private val keywordArg: FacilitySearchArg.Keyword? = facilitySearchArg.takeIf { it is FacilitySearchArg.Keyword }
        ?.let { it as FacilitySearchArg.Keyword }

    private val serviceArg: FacilitySearchArg.Service? = facilitySearchArg.takeIf { it is FacilitySearchArg.Service }
        ?.let { it as FacilitySearchArg.Service }

    private val _detailConditions: MutableStateFlow<DetailFilterConditionsUiState?> =
        MutableStateFlow(serviceArg?.type?.let { DetailFilterConditionsUiState.from(it) })
    val detailConditions: StateFlow<DetailFilterConditionsUiState?> = _detailConditions.asStateFlow()

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

    private val _administrativeDongs: StateFlow<Map<Borough, List<AdministrativeDong>>> =
        administrativeDongRepository.administrativeDongs
            .map { it.values.groupBy { dong -> dong.borough } }
            .viewModelStateIn(initialValue = emptyMap())

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

    private val regionsOfSelectedBorough: StateFlow<List<RegionUiState>> = combine(
        _administrativeDongs,
        selectedBorough.filterNotNull(),
    ) { dongs, selectedBorough ->
        listOf(RegionUiState.Borough(selectedBorough)) +
                (dongs[selectedBorough]?.map { RegionUiState.Dong(it) } ?: emptyList())
    }.viewModelStateIn(initialValue = emptyList())

    private val _selectedRegions: MutableStateFlow<List<RegionUiState>> = MutableStateFlow(emptyList())
    val selectedRegions: StateFlow<List<RegionUiState>> = _selectedRegions.asStateFlow()

    val currentSelectableRegions: StateFlow<List<SelectableRegionUiState>> = combine(
        regionsOfSelectedBorough,
        _selectedRegions
    ) { selectableRegions, selectedRegions ->
        selectableRegions.map {
            SelectableRegionUiState(
                isSelected = it in selectedRegions,
                region = it,
            )
        }
    }.viewModelStateIn(initialValue = emptyList())

    val keyword: MutableStateFlow<String> = MutableStateFlow(keywordArg?.keyword ?: "")

    private val _appliedConditions: MutableStateFlow<FilterConditionsUiState> = MutableStateFlow(
        FilterConditionsUiState(
            keyword = keywordArg?.keyword ?: "",
            detailConditions = serviceArg?.type?.let { DetailFilterConditionsUiState.from(it) }
        )
    )
    val appliedConditions: StateFlow<FilterConditionsUiState> = _appliedConditions.asStateFlow()

    private val _facilityOrder: MutableStateFlow<FacilityOrder> = MutableStateFlow(FacilityOrder.ReviewCount())
    val facilityOrder: StateFlow<FacilityOrder> = _facilityOrder.asStateFlow()

    val facilitiesCount: StateFlow<Int> = appliedConditions.map {
        facilityRepository.getFacilitiesCount(it.asFacilityFilterConditions())
    }.viewModelStateIn(initialValue = 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val facilities: Flow<PagingData<Facility>> = combine(
        _appliedConditions,
        _facilityOrder
    ) { appliedConditions, facilityOrder ->
        appliedConditions to facilityOrder
    }.onEach { _isLoading.value = true }
        .flatMapLatest { (appliedConditions, facilityOrder) ->
            Pager(PagingConfig(pageSize = 30)) {
                FacilitiesPagingSource(
                    facilityRepository = facilityRepository,
                    filterConditions = appliedConditions.asFacilityFilterConditions(),
                    facilityOrder = facilityOrder,
                )
            }.flow
                .cachedIn(viewModelScope)
        }.onEach { _isLoading.value = false }

    fun selectReviewCountOrder() {
        _facilityOrder.value = FacilityOrder.ReviewCount()
    }

    fun selectStarPointAvgOrder() {
        _facilityOrder.value = FacilityOrder.StarPointAvg()
    }

    fun setChildCareDetailConditions() {
        if (_detailConditions.value is ChildCareFilterConditionsUiState) return
        _detailConditions.value = ChildCareFilterConditionsUiState()
    }

    fun setKidsCafeDetailConditions() {
        if (_detailConditions.value is KidsCafeConditionsUiState) return
        _detailConditions.value = KidsCafeConditionsUiState()
    }

    fun setOtherDetailConditions() {
        if (_detailConditions.value is OtherDetailFilterConditionsUiState) return
        _detailConditions.value = OtherDetailFilterConditionsUiState()
    }

    fun setChildCareFacilityType(childCareFacilityType: ChildCareFacilityType) {
        val detailConditions = _detailConditions.value
        if (detailConditions !is ChildCareFilterConditionsUiState) return
        _detailConditions.value = detailConditions.copy(type = childCareFacilityType)
    }

    fun setMustOperateSaturday(value: Boolean) {
        val detailConditions = _detailConditions.value
        if (detailConditions !is ChildCareFilterConditionsUiState) return
        _detailConditions.value = detailConditions.copy(mustSaturdayOperate = value)
    }

    fun toggleDayOfWeek(dayOfWeek: DayOfWeek) {
        val detailConditions = _detailConditions.value
        if (detailConditions !is KidsCafeConditionsUiState) return
        if (dayOfWeek in detailConditions.daysOfWeek) {
            _detailConditions.value = detailConditions.copy(daysOfWeek = detailConditions.daysOfWeek - dayOfWeek)
        } else {
            _detailConditions.value = detailConditions.copy(daysOfWeek = detailConditions.daysOfWeek + dayOfWeek)
        }
    }

    fun setOtherFacilityType(otherFacilityType: OtherFacilityType) {
        val detailConditions = _detailConditions.value
        if (detailConditions !is OtherDetailFilterConditionsUiState) return
        _detailConditions.value = detailConditions.copy(type = otherFacilityType)
    }

    fun changeConditionsToFitKid(kidId: String) {
        val kid = kids.value.find { it.id == kidId } ?: return
        age.value = kid.age.toString()
        _selectedRegions.value = listOf(RegionUiState.Dong(kid.livingDong))
    }

    fun selectBorough(boroughId: Long) {
        selectedBorough.value = _boroughs.value.firstOrNull { it.id == boroughId }
    }

    // TODO: 여기 코드 리팩토링 해야함
    fun addBoroughCondition(boroughId: Long) {
        _selectedRegions.value = (_selectedRegions.value.filterNot { it is RegionUiState.Dong && it.administrativeDong.borough.id == boroughId } +
                RegionUiState.Borough(_boroughs.value.find { it.id == boroughId } ?: return)).distinct()
    }

    fun removeBoroughCondition(boroughId: Long) {
        _selectedRegions.value = _selectedRegions.value.filterNot { it is RegionUiState.Borough && it.borough.id == boroughId }
    }

    fun addDongCondition(administrativeDongId: Long) {
        val dong = _administrativeDongs.value.values.flatten().find { it.id == administrativeDongId } ?: return
        _selectedRegions.value = (_selectedRegions.value.filterNot { it is RegionUiState.Borough && it.borough.id == dong.borough.id } + RegionUiState.Dong(dong)).distinct()
    }

    fun removeDongCondition(administrativeDongId: Long) {
        _selectedRegions.value = _selectedRegions.value.filterNot { it is RegionUiState.Dong && it.administrativeDong.id == administrativeDongId }
    }

    fun resetFilterConditions() {
        _detailConditions.value = null
        age.value = ""
        _selectedRegions.value = emptyList()
    }

    fun applyFilterConditions() {
        val selectedBoroughs = _selectedRegions.value
            .filterIsInstance<RegionUiState.Borough>()
            .map { it.borough }
        val selectedAdministrativeDongs = _selectedRegions.value
            .filterIsInstance<RegionUiState.Dong>()
            .map { it.administrativeDong }
        _appliedConditions.value = _appliedConditions.value.copy(
            keyword = keyword.value,
            detailConditions = _detailConditions.value,
            age = age.value.toIntOrNull(),
            boroughs = selectedBoroughs.toSet(),
            administrativeDongs = selectedAdministrativeDongs.toSet(),
        )
    }

    fun searchWithKeyword() {
        _appliedConditions.value = _appliedConditions.value.copy(keyword = keyword.value)
    }

    companion object {

        private val NOT_NUMBER_REGEX = Regex("[^0-9]")
    }
}
