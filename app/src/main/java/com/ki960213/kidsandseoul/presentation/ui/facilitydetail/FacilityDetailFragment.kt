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
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FacilityDetailFragment :
    BaseFragment<FragmentFacilityDetailBinding>(R.layout.fragment_facility_detail) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val facilityDetailViewModel: FacilityDetailViewModel by viewModels()

    private var isNotLogin: Boolean = true
    private var isAlreadyReviewRegistered: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupViewPager()
        setupTabLayout()

        observeLoginUser()
        observeIsAlreadyReviewRegistered()
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
        if (isNotLogin) {
            mainViewModel.dispatchLoginEvent()
            return
        }
        facilityDetailViewModel.changeInterestFacility(willInterestAdd)
    }

    private fun handleReviewButtonClick() {
        if (isNotLogin) {
            mainViewModel.dispatchLoginEvent()
            return
        }
        if (isAlreadyReviewRegistered) {
            requireContext().showToast(R.string.facility_detail_review_already_registered)
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
        binding.vpFacilityDetail.isUserInputEnabled = false
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tlFacilityDetail, binding.vpFacilityDetail) { tab, position ->
            tab.text = when (position) {
                0 -> "기본 정보"
                else -> "후기"
            }
        }.attach()
    }

    private fun observeLoginUser() {
        repeatOnStarted {
            facilityDetailViewModel.loginUser.collect { isNotLogin = it == null }
        }
    }

    private fun observeIsAlreadyReviewRegistered() {
        repeatOnStarted {
            facilityDetailViewModel.isAlreadyReviewRegistered.collect {
                isAlreadyReviewRegistered = it
            }
        }
    }

    companion object {

        const val KEY_FACILITY_ID = "facilityId"
    }
}
