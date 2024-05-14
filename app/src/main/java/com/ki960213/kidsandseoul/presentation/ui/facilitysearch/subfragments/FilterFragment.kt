package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.subfragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.kid.model.Kid
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFilterBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.MainViewModel
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.FacilitySearchFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.FacilitySearchViewModel
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.kids.KidsAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.selectableregions.SelectableRegionsAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.selectedregions.SelectedRegionsAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.RegionUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.SelectableRegionUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(R.layout.fragment_filter) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val facilitySearchViewModel: FacilitySearchViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupRecyclerViews()
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = facilitySearchViewModel
        binding.onCloseButtonClick = { parentFragmentManager.popBackStack() }
        binding.onKidAddButtonClick = ::handleKidAddButtonClick
        binding.onApplyButtonClick = ::handleApplyButtonClick
    }

    private fun handleKidAddButtonClick() {
        val loginUserId = facilitySearchViewModel.loginUserId.value
        if (loginUserId == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            val direction = FacilitySearchFragmentDirections.actionFacilitySearchFragmentToKidAddFragment(loginUserId)
            requireParentFragment().findNavController().navigateSafely(direction)
        }
    }

    private fun handleApplyButtonClick() {
        facilitySearchViewModel.applyFilterConditions()
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun setupRecyclerViews() {
        binding.rvFilterKids.adapter = KidsAdapter(facilitySearchViewModel::changeConditionsToFitKid)
        binding.rvFilterBoroughs.adapter = BoroughsAdapter(facilitySearchViewModel::selectBorough)
        binding.rvFilterAdministrativeDongs.adapter = SelectableRegionsAdapter(
            onBoroughClick = facilitySearchViewModel::addBoroughCondition,
            onDongClick = facilitySearchViewModel::addDongCondition
        )
        binding.rvFilterSelectedRegions.adapter = SelectedRegionsAdapter(
            onBoroughClick = facilitySearchViewModel::removeBoroughCondition,
            onDongClick = facilitySearchViewModel::removeDongCondition
        )
    }
}

@BindingAdapter("app:filter_kids")
fun RecyclerView.setKids(kids: List<Kid>) {
    (adapter as KidsAdapter).submitList(kids)
}

@BindingAdapter("app:filter_currentSelectableRegions")
fun RecyclerView.setCurrentSelectableRegions(selectableRegions: List<SelectableRegionUiState>) {
    (adapter as SelectableRegionsAdapter).submitList(selectableRegions)
}

@BindingAdapter("app:filter_selectedRegions")
fun RecyclerView.setSelectedRegions(selectedRegions: List<RegionUiState>) {
    (adapter as SelectedRegionsAdapter).submitList(selectedRegions)
}
