package com.ki960213.kidsandseoul.presentation.ui.firstscreen.home

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.facility.model.Facility
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentHomeBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import com.ki960213.kidsandseoul.presentation.ui.MainActivity
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.FacilitySearchArg
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.FirstScreenFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.home.interestfacilities.InterestFacilitiesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupSearchInput()
        setupInterestFacilitiesRecyclerView()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onNotificationsButtonClick = ::navigateToNotifications
        binding.onSearchButtonClick = ::handleSearch
        binding.onFacilityServiceClick = ::navigateToFacilitiesByService
    }

    private fun navigateToNotifications() {
        val direction =
            FirstScreenFragmentDirections.actionFirstScreenFragmentToNotificationsFragment()
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun handleSearch(keyword: String) {
        if (keyword.trim().length < 2) {
            requireContext().showToast(R.string.all_search_too_short_message)
            return
        }
        binding.etHomeInput.clearFocus()
        binding.etHomeInput.text.clear()
        val direction = FirstScreenFragmentDirections
            .actionFirstScreenFragmentToFacilitySearchFragment(FacilitySearchArg.Keyword(keyword))
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun navigateToFacilitiesByService(service: ServiceUiState) {
        val direction = FirstScreenFragmentDirections
            .actionFirstScreenFragmentToFacilitySearchFragment(FacilitySearchArg.Service(service))
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun setupSearchInput() {
        binding.etHomeInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleSearch(binding.etHomeInput.text.toString())
            }
            false
        }
    }

    private fun setupInterestFacilitiesRecyclerView() {
        binding.rvInterestFacilities.adapter = InterestFacilitiesAdapter(::navigateToFacilityDetail)
    }

    private fun navigateToFacilityDetail(facilityId: Long) {
        val direction = FirstScreenFragmentDirections
            .actionFirstScreenFragmentToFacilityDetailFragment(facilityId)
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }
}

@BindingAdapter("app:home_interestFacilities")
fun RecyclerView.setInterestFacilities(facilities: List<Facility>) {
    (adapter as InterestFacilitiesAdapter).submitList(facilities)
}
