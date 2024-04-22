package com.ki960213.kidsandseoul.presentation.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentWebViewBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : BaseFragment<FragmentWebViewBinding>(R.layout.fragment_web_view) {

    private val args: WebViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupTitle()
        setupWebView()
    }

    private fun setupDataBinding() {
        binding.onBackButtonClick = { findNavController().popBackStack() }
    }

    private fun setupTitle() {
        binding.tvWebViewTitle.text = args.title
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.wvWebView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            loadUrl(args.url)
        }
    }

    companion object {

        const val USE_OF_TERM_URL =
            "https://rustic-quill-9a2.notion.site/b5e44608a71b4310ae0a2cfc8f0c94dd?pvs=4"
        const val PRIVACY_POLICY_URL =
            "https://rustic-quill-9a2.notion.site/b5e44608a71b4310ae0a2cfc8f0c94dd?pvs=4"
        const val OUR_NEIGHBOR_GROWING_CENTER_INFORMATION_USE_URL =
            "https://icare.seoul.go.kr/icare/user/fcltyInfoManage/BD_selectCareCenterContactList.do?q_tap=1"
        const val LOCAL_CHILDREN_CENTER_INFORMATION_USE_URL =
            "https://icare.seoul.go.kr/icare/user/fcltyInfoManage/BD_selectLocalCenterContactList.do?q_tap=1"
        const val CO_PARENTING_ROOM_INFORMATION_USE_URL =
            "https://icare.seoul.go.kr/icare/user/fcltyInfoManage/BD_selectCopertnChldcrContactList.do?q_svcClCode=1002&q_tap=1"
        const val CO_PARENTING_SHARING_CENTER_INFORMATION_USE_URL =
            "https://icare.seoul.go.kr/icare/user/fcltyInfoManage/BD_selectCopertnChldcrContactList.do?q_svcClCode=1004&q_tap=1"
        const val KIDS_CAFE_INFORMATION_USE_URL =
            "https://icare.seoul.go.kr/icare/dolbomMENU5/dolbomMENU5_1.jsp"
    }
}
