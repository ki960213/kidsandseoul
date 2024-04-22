package com.ki960213.kidsandseoul.presentation.ui.facilitydetail.detailinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.domain.facility.model.ChildCareFacility
import com.ki960213.domain.facility.model.ChildCareService
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.KidsCafe
import com.ki960213.domain.facility.model.OtherFacility
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilityDetailInfoBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.FacilityDetailFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.FacilityDetailViewModel
import com.ki960213.kidsandseoul.presentation.ui.webview.WebViewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FacilityDetailInfoFragment :
    BaseFragment<FragmentFacilityDetailInfoBinding>(R.layout.fragment_facility_detail_info) {

    private val viewModel: FacilityDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onAddressCopyButtonClick = ::handleAddressCopyButtonClick
        binding.onInformationUseClick = ::handleInformationUseClick
        binding.onHomepageButtonClick = ::handleHomepageButtonClick
    }

    private fun handleAddressCopyButtonClick() {
        val facility = viewModel.facility.value ?: return
        val text = "${facility.address.baseAddress} ${facility.address.detail}"
        requireContext().copyToClipboard(text)
        // 33 이상부터는 클립보드에 복사할 때 기본적으로 토스트 메세지를 보여줍니다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) requireContext().showToast(R.string.facility_detail_info_address_copy_message)
    }

    private fun Context.copyToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, text)
        clipboardManager.setPrimaryClip(clipData)
    }

    private fun handleInformationUseClick(facility: Facility) {
        when (facility) {
            is ChildCareFacility -> when (facility.childCareService) {
                ChildCareService.OUR_NEIGHBORHOOD_GROWING_CENTER -> navigateToWebView(
                    title = "우리동네키움센터 소개",
                    url = WebViewFragment.OUR_NEIGHBOR_GROWING_CENTER_INFORMATION_USE_URL
                )

                ChildCareService.CO_PARENTING_ROOM -> navigateToWebView(
                    title = "공동육아방 소개",
                    url = WebViewFragment.CO_PARENTING_ROOM_INFORMATION_USE_URL
                )

                ChildCareService.LOCAL_CHILDREN_CENTER -> navigateToWebView(
                    title = "지역아동센터 소개",
                    url = WebViewFragment.LOCAL_CHILDREN_CENTER_INFORMATION_USE_URL
                )

                ChildCareService.CO_PARENTING_SHARING_CENTER -> navigateToWebView(
                    title = "공동육아나눔터 소개",
                    url = WebViewFragment.CO_PARENTING_SHARING_CENTER_INFORMATION_USE_URL
                )

                ChildCareService.YOUTH_AFTER_SCHOOL_ACADEMY -> return
            }

            is KidsCafe -> navigateToWebView(
                "키즈 카페 소개",
                WebViewFragment.KIDS_CAFE_INFORMATION_USE_URL
            )

            is OtherFacility -> return
        }
    }

    private fun handleHomepageButtonClick(facility: Facility) {
        navigateToWebView(facility.name, facility.detailUrl)
    }

    private fun navigateToWebView(title: String, url: String) {
        val direction = FacilityDetailFragmentDirections
            .actionFacilityDetailFragmentToWebViewFragment(title = title, url = url)
        findNavController().navigateSafely(direction)
    }
}
