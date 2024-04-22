package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

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
import com.ki960213.domain.facility.model.ChildCareService
import com.ki960213.domain.facility.model.Facilities
import com.ki960213.domain.facility.model.FacilityType
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilitiesBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.facilities.FacilitiesAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.filtercondition.FilterConditionAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek

@AndroidEntryPoint
class FacilitiesFragment : BaseFragment<FragmentFacilitiesBinding>(R.layout.fragment_facilities) {

    private val viewModel: FacilitySearchViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupBackPressed()
        setupFilterConditionItemsRecyclerView()
        setupFacilitiesRecyclerView()
        setupSearchInput()

        observeUiEvent()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
        binding.onSearchButtonClick = { viewModel.setKeyword(it) }
        binding.onFilterButtonClick = ::navigateToFilter
    }

    private fun handleSearch(keyword: String) {
        if (keyword.trim().length < 2) {
            requireContext().showToast(R.string.all_search_too_short_message)
            return
        }
        binding.etFacilitiesNameInput.clearFocus()
        viewModel.setKeyword(keyword)
    }

    private fun navigateToFilter() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            add<FilterFragment>(R.id.fcv_facility_search)
            addToBackStack(null)
        }
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

    private fun setupFilterConditionItemsRecyclerView() {
        binding.rvFacilitiesFilterConditionItems.adapter = FilterConditionAdapter()
    }

    private fun setupFacilitiesRecyclerView() {
        binding.rvFacilities.adapter = FacilitiesAdapter(::navigateToFacilityDetail)
    }

    private fun navigateToFacilityDetail(facilityId: Long) {
        val direction = FacilitySearchFragmentDirections
            .actionFacilitySearchFragmentToFacilityDetailFragment(facilityId)
        findNavController().navigateSafely(direction)
    }

    private fun setupSearchInput() {
        binding.etFacilitiesNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleSearch(binding.etFacilitiesNameInput.text.toString())
            }
            false
        }
    }

    private fun observeUiEvent() {
        repeatOnStarted {
            viewModel.uiEvent.collect {
                when (it) {
                    FacilitiesUiEvent.KeywordTooShort -> requireContext().showToast(R.string.all_search_too_short_message)
                }
            }
        }
    }
}

@BindingAdapter("app:facilities_filterCondition")
fun RecyclerView.setFilterCondition(condition: FilterConditionUiState) {
    var items = condition.detailCondition?.let { context.createConditionItems(it) } ?: emptyList()
    if (condition.age != null) {
        items = items + context.getString(
            R.string.facilities_age_condition_item_format,
            condition.age
        )
    }
    if (condition.region != null) {
        items = items + "${condition.region.borough.name} ${condition.region.name}"
    }
    (adapter as FilterConditionAdapter).submitList(items)
}

private fun Context.createConditionItems(
    detailFilterConditionUiState: DetailFilterConditionUiState,
): List<String> = when (detailFilterConditionUiState) {
    is ChildCareFilterConditionUiState -> createConditionItems(detailFilterConditionUiState)
    is KidsCafeConditionUiState -> createConditionItems(detailFilterConditionUiState)
    is OtherDetailFilterConditionUiState -> createConditionItems(detailFilterConditionUiState)
}

private fun Context.createConditionItems(
    childCareFilterConditionUiState: ChildCareFilterConditionUiState,
): List<String> = when (childCareFilterConditionUiState.service) {
    ChildCareService.OUR_NEIGHBORHOOD_GROWING_CENTER -> listOf(getString(R.string.all_our_neighbor_growing_center))
    ChildCareService.CO_PARENTING_ROOM -> listOf(getString(R.string.all_co_parenting_room))
    ChildCareService.LOCAL_CHILDREN_CENTER -> listOf(getString(R.string.all_local_children_center))
    ChildCareService.CO_PARENTING_SHARING_CENTER -> listOf(getString(R.string.all_co_parenting_sharing_center))
    ChildCareService.YOUTH_AFTER_SCHOOL_ACADEMY -> throw AssertionError("청소년 방과후 아카데미는 안다룸")
    null -> listOf()
}.let { if (childCareFilterConditionUiState.mustSaturdayOperate) it + getString(R.string.filter_saturday_operation) else it }

private fun Context.createConditionItems(
    kidsCafeConditionUiState: KidsCafeConditionUiState,
): List<String> = listOf(getString(R.string.all_kids_cafe)).let {
    if (kidsCafeConditionUiState.daysOfWeek.isNotEmpty()) {
        it + listOf(
            kidsCafeConditionUiState.daysOfWeek
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

private fun Context.createConditionItems(
    otherFilterConditionUiState: OtherDetailFilterConditionUiState,
): List<String> = when (otherFilterConditionUiState.type) {
    FacilityType.OUTDOOR -> listOf(getString(R.string.all_outdoor))
    FacilityType.EXPERIENCE -> listOf(getString(R.string.all_experience))
    FacilityType.MEDICAL -> listOf(getString(R.string.all_medical))
    FacilityType.LIBRARY -> listOf(getString(R.string.all_library))
    null -> listOf()
}

@BindingAdapter("app:facilities_facilities")
fun RecyclerView.setFacilities(facilities: Facilities) {
    (adapter as FacilitiesAdapter).submitList(facilities.value)
}
