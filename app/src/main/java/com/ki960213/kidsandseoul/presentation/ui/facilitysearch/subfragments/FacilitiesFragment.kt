package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.subfragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.addCallback
import androidx.databinding.BindingAdapter
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.facility.model.ChildCareFacilityType
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.OtherFacilityType
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilitiesBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.FacilitySearchFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.FacilitySearchViewModel
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.facilities.FacilitiesAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.facilities.FacilitiesAdapter1
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.filterconditions.FilterConditionsAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.ChildCareFilterConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.DetailFilterConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.FilterConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.KidsCafeConditionsUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.OtherDetailFilterConditionsUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.DayOfWeek

@AndroidEntryPoint
class FacilitiesFragment : BaseFragment<FragmentFacilitiesBinding>(R.layout.fragment_facilities) {

    private val viewModel: FacilitySearchViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupBackPressed()
        setupFilterConditionsItemsRecyclerView()
        setupFacilitiesRecyclerView()
        setupSearchInput()

        observeFacilities()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
        binding.onSearchButtonClick = viewModel::searchWithKeyword
        binding.onFilterButtonClick = ::navigateToFilter
        binding.onOrderButtonClick = ::showSelectOrderDialog
    }

    private fun navigateToFilter() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            add<FilterFragment>(R.id.fcv_facility_search)
            addToBackStack(null)
        }
    }

    private fun showSelectOrderDialog() {
        SelectFacilityOrderDialog().show(parentFragmentManager, null)
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (parentFragmentManager.backStackEntryCount == 0) {
                findNavController().popBackStack()
            } else {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun setupFilterConditionsItemsRecyclerView() {
        binding.rvFacilitiesFilterConditionsItems.adapter = FilterConditionsAdapter()
    }

    private fun setupFacilitiesRecyclerView() {
        binding.rvFacilities.adapter = FacilitiesAdapter1(::navigateToFacilityDetail)
    }

    private fun navigateToFacilityDetail(facilityId: Long) {
        val direction = FacilitySearchFragmentDirections
            .actionFacilitySearchFragmentToFacilityDetailFragment(facilityId)
        findNavController().navigateSafely(direction)
    }

    private fun setupSearchInput() {
        binding.etFacilitiesNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) viewModel.applyFilterConditions()
            binding.etFacilitiesNameInput.clearFocus()
            false
        }
    }

    private fun observeFacilities() {
        repeatOnStarted {
            viewModel.facilities.collectLatest {
                (binding.rvFacilities.adapter as FacilitiesAdapter1).submitData(it)
            }
        }
    }
}

@BindingAdapter("app:facilities_filterConditions")
fun RecyclerView.setFilterConditions(conditions: FilterConditionsUiState) {
    var items = conditions.detailConditions?.let { context.createConditionsItems(it) } ?: emptyList()
    if (conditions.age != null) {
        items = items + context.getString(
            R.string.facilities_age_conditions_item_format,
            conditions.age
        )
    }
    val boroughs = conditions.boroughs.map {
        "${it.name} 전체"
    }
    val dongs = conditions.administrativeDongs.map {
        "${it.borough.name} ${it.name}"
    }
    items = items + boroughs + dongs
    (adapter as FilterConditionsAdapter).submitList(items)
}

private fun Context.createConditionsItems(
    detailFilterConditionsUiState: DetailFilterConditionsUiState,
): List<String> = when (detailFilterConditionsUiState) {
    is ChildCareFilterConditionsUiState -> createConditionsItems(detailFilterConditionsUiState)
    is KidsCafeConditionsUiState -> createConditionsItems(detailFilterConditionsUiState)
    is OtherDetailFilterConditionsUiState -> createConditionsItems(detailFilterConditionsUiState)
}

private fun Context.createConditionsItems(
    childCareFilterConditionsUiState: ChildCareFilterConditionsUiState,
): List<String> = when (childCareFilterConditionsUiState.type) {
    ChildCareFacilityType.OUR_NEIGHBORHOOD_GROWING_CENTER -> listOf(getString(R.string.all_our_neighbor_growing_center))
    ChildCareFacilityType.CO_PARENTING_ROOM -> listOf(getString(R.string.all_co_parenting_room))
    ChildCareFacilityType.LOCAL_CHILDREN_CENTER -> listOf(getString(R.string.all_local_children_center))
    ChildCareFacilityType.CO_PARENTING_SHARING_CENTER -> listOf(getString(R.string.all_co_parenting_sharing_center))
    null -> listOf(getString(R.string.all_child_care))
}.let { if (childCareFilterConditionsUiState.mustSaturdayOperate) it + getString(R.string.filter_saturday_operation) else it }

private fun Context.createConditionsItems(
    kidsCafeConditionsUiState: KidsCafeConditionsUiState,
): List<String> = listOf(getString(R.string.all_kids_cafe)).let {
    if (kidsCafeConditionsUiState.daysOfWeek.isNotEmpty()) {
        it + listOf(
            kidsCafeConditionsUiState.daysOfWeek
                .sorted()
                .joinToString(prefix = "", postfix = "") { dayOfWeek ->
                    when (dayOfWeek) {
                        DayOfWeek.MONDAY -> "월"
                        DayOfWeek.TUESDAY -> "화"
                        DayOfWeek.WEDNESDAY -> "수"
                        DayOfWeek.THURSDAY -> "목"
                        DayOfWeek.FRIDAY -> "금"
                        DayOfWeek.SATURDAY -> "토"
                        DayOfWeek.SUNDAY -> "일"
                    }
                }
        )
    } else it
}

private fun Context.createConditionsItems(
    otherFilterConditionsUiState: OtherDetailFilterConditionsUiState,
): List<String> = when (otherFilterConditionsUiState.type) {
    OtherFacilityType.OUTDOOR -> listOf(getString(R.string.all_outdoor))
    OtherFacilityType.EXPERIENCE -> listOf(getString(R.string.all_experience))
    OtherFacilityType.MEDICAL -> listOf(getString(R.string.all_medical))
    OtherFacilityType.LIBRARY -> listOf(getString(R.string.all_library))
    null -> listOf(getString(R.string.all_other_facility))
}

@BindingAdapter("app:facilities_facilities")
fun RecyclerView.setFacilities(facilities: List<Facility>) {
    (adapter as FacilitiesAdapter).submitList(facilities)
}
