package com.ki960213.kidsandseoul.presentation.ui.myinterestfacilities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentMyInterestFacilitiesBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.facilities.FacilitiesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyInterestFacilitiesFragment : BaseFragment<FragmentMyInterestFacilitiesBinding>(R.layout.fragment_my_interest_facilities) {

    private val viewModel: MyInterestFacilitiesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupInterestFacilitiesRecyclerView()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
    }

    private fun setupInterestFacilitiesRecyclerView() {
        binding.rvMyInterestFacilities.adapter = FacilitiesAdapter(::navigateToFacilityDetail)
    }

    private fun navigateToFacilityDetail(facilityId: Long) {
        val direction = MyInterestFacilitiesFragmentDirections
            .actionMyInterestFacilitiesFragmentToFacilityDetailFragment(facilityId)
        findNavController().navigateSafely(direction)
    }

    companion object {

        const val KEY_USER_ID = "userId"
    }
}
