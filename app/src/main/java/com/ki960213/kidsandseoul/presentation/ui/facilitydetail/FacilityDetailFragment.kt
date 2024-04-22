package com.ki960213.kidsandseoul.presentation.ui.facilitydetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.ki960213.domain.facility.model.KidsCafe
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilityDetailBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FacilityDetailFragment :
    BaseFragment<FragmentFacilityDetailBinding>(R.layout.fragment_facility_detail) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val facilityDetailViewModel: FacilityDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupViewPager()
        setupTabLayout()
    }

    private fun setupDataBinding() {
        binding.viewModel = facilityDetailViewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
        binding.onCallButtonClick = ::callToKidsCafe
        binding.onInterestButtonClick = ::handleInterestButtonClick
        binding.onReviewButtonClick = ::handleReviewButtonClick
    }

    private fun callToKidsCafe() {
        val facility = facilityDetailViewModel.facility.value
        if (facility !is KidsCafe) return
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${facility.contact}")
        startActivity(intent)
    }

    private fun handleInterestButtonClick(willInterestAdd: Boolean) {
        if (facilityDetailViewModel.loginUser.value == null) {
            mainViewModel.dispatchLoginEvent()
            return
        }
        facilityDetailViewModel.changeInterestFacility(willInterestAdd)
    }

    private fun handleReviewButtonClick() {
        if (facilityDetailViewModel.loginUser.value == null) {
            mainViewModel.dispatchLoginEvent()
            return
        }
        navigateToReviewWrite()
    }

    private fun navigateToReviewWrite() {
        val facilityId = facilityDetailViewModel.facility.value?.id ?: return
        val direction = FacilityDetailFragmentDirections
            .actionFacilityDetailFragmentToReviewWriteFragment(facilityId)
        findNavController().navigateSafely(direction)
    }

    private fun setupViewPager() {
        binding.vpFacilityDetail.adapter = FacilityDetailFragmentsAdapter(this)
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tlFacilityDetail, binding.vpFacilityDetail) { tab, position ->
            tab.text = when (position) {
                0 -> "기본 정보"
                else -> "후기"
            }
        }.attach()
    }

    companion object {

        const val KEY_FACILITY_ID = "facilityId"
    }
}
