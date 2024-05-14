package com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile

import android.os.Bundle
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentMyProfileBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.ui.MainActivity
import com.ki960213.kidsandseoul.presentation.ui.MainViewModel
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.FirstScreenFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.dialog.LeaveConfirmDialog
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.dialog.LogoutConfirmDialog
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids.KidsAdapter
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids.KidsItemUiState
import com.ki960213.kidsandseoul.presentation.ui.webview.WebViewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(R.layout.fragment_my_profile) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val myProfileViewModel: MyProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupKidsRecyclerView()

        observeUiEvent()
    }

    private fun setupDataBinding() {
        binding.viewModel = myProfileViewModel
        binding.onProfileSettingButtonClick = ::navigateToProfileEdit
        binding.onLoginClick = mainViewModel::dispatchLoginEvent
        binding.onMyInterestButtonClick = ::handleMyInterestButtonClick
        binding.onMyReviewButtonClick = ::handleMyReviewButtonClick
        binding.onFollowButtonClick = ::handleFollowerButtonClick
        binding.onFollowingButtonClick = ::handleFollowingButtonClick
        binding.onMyPostButtonClick = ::handleMyPostClick
        binding.onMyCommentButtonClick = ::handleMyCommentClick
        binding.onUseOfTermButtonClick = ::navigateToUseOfTerm
        binding.onPrivacyPolicyButtonClick = ::navigateToPrivacyPolicy
        binding.onLogoutButtonClick = ::showLogoutConfirmDialog
        binding.onLeaveButtonClick = ::showLeaveConfirmDialog
    }

    private fun navigateToProfileEdit() {
        val parentId = myProfileViewModel.loginUser.value?.id ?: return
        val direction =
            FirstScreenFragmentDirections.actionFirstScreenFragmentToProfileEditFragment(parentId)
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun handleMyInterestButtonClick() {
        val loginUserId = myProfileViewModel.loginUser.value?.id
        if (loginUserId == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            val direction = FirstScreenFragmentDirections
                .actionFirstScreenFragmentToMyInterestFacilitiesFragment(loginUserId)
            (requireActivity() as MainActivity).navController.navigateSafely(direction)
        }
    }

    private fun handleMyReviewButtonClick() {
        val loginUserId = myProfileViewModel.loginUser.value?.id
        if (loginUserId == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            val direction = FirstScreenFragmentDirections
                .actionFirstScreenFragmentToWrittenReviewsFragment(loginUserId)
            (requireActivity() as MainActivity).navController.navigateSafely(direction)
        }
    }

    private fun handleFollowerButtonClick() {
        val loginUserId = myProfileViewModel.loginUser.value?.id
        if (loginUserId == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            val direction = FirstScreenFragmentDirections
                .actionFirstScreenFragmentToFollowersFragment(loginUserId)
            (requireActivity() as MainActivity).navController.navigateSafely(direction)
        }
    }

    private fun handleFollowingButtonClick() {
        val loginUserId = myProfileViewModel.loginUser.value?.id
        if (loginUserId == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            val direction = FirstScreenFragmentDirections
                .actionFirstScreenFragmentToFollowingsFragment(loginUserId)
            (requireActivity() as MainActivity).navController.navigateSafely(direction)
        }
    }

    private fun handleMyPostClick() {
        val loginUserId = myProfileViewModel.loginUser.value?.id
        if (loginUserId == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            val direction = FirstScreenFragmentDirections
                .actionFirstScreenFragmentToWrittenPostsFragment(loginUserId)
            (requireActivity() as MainActivity).navController.navigateSafely(direction)
        }
    }

    private fun handleMyCommentClick() {
        val loginUserId = myProfileViewModel.loginUser.value?.id
        if (loginUserId == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            val direction = FirstScreenFragmentDirections
                .actionFirstScreenFragmentToWrittenCommentsFragment(loginUserId)
            (requireActivity() as MainActivity).navController.navigateSafely(direction)
        }
    }

    private fun navigateToUseOfTerm() {
        val direction = FirstScreenFragmentDirections.actionFirstScreenFragmentToWebViewFragment(
            title = getString(R.string.all_use_of_term),
            url = WebViewFragment.USE_OF_TERM_URL,
        )
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun navigateToPrivacyPolicy() {
        val direction = FirstScreenFragmentDirections.actionFirstScreenFragmentToWebViewFragment(
            title = getString(R.string.all_privacy_policy),
            url = WebViewFragment.PRIVACY_POLICY_URL,
        )
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun showLogoutConfirmDialog() {
        LogoutConfirmDialog().show(childFragmentManager, null)
    }

    private fun showLeaveConfirmDialog() {
        LeaveConfirmDialog().show(childFragmentManager, null)
    }

    private fun setupKidsRecyclerView() {
        binding.rvMyProfileKids.adapter = KidsAdapter(onKidAddClick = ::handleKidAddClick)
    }

    private fun handleKidAddClick() {
        if (myProfileViewModel.isLogin.value) {
            navigateToKidAdd()
        } else {
            mainViewModel.dispatchLoginEvent()
        }
    }

    private fun navigateToKidAdd() {
        val navController = parentFragment?.parentFragment?.findNavController() ?: return
        val parentId = myProfileViewModel.loginUser.value?.id ?: return
        val direction =
            FirstScreenFragmentDirections.actionFirstScreenFragmentToKidAddFragment(parentId)
        navController.navigateSafely(direction)
    }

    private fun observeUiEvent() {
        repeatOnStarted {
            myProfileViewModel.uiEvent.collect(::handleUiEvent)
        }
    }

    private fun handleUiEvent(uiEvent: MyProfileUiEvent) {
        when (uiEvent) {
            MyProfileUiEvent.LeaveFail -> requireContext().showToast(R.string.my_profile_leave_fail)
        }
    }
}

@BindingAdapter("app:profile_kids")
fun RecyclerView.setKids(kids: List<KidsItemUiState>) {
    val kidsAdapter = adapter as KidsAdapter
    kidsAdapter.submitList(kids)
}
