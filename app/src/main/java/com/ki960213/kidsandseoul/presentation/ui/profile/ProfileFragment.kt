package com.ki960213.kidsandseoul.presentation.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentProfileBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.ui.MainViewModel
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.reviews.ReviewImageAdapter
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids.KidsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupKidsRecyclerView()

        observeProfileUiEvent()
    }

    private fun setupDataBinding() {
        binding.viewModel = profileViewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
        binding.onFollowerClick = ::navigateToFollowers
        binding.onFollowingClick = ::navigateToFollowings
        binding.onFollowToggle = ::handleFollowToggle
        binding.includeProfilePost.onPostClick = ::navigateToPostDetail
        binding.onWrittenPostMoreButtonClick = ::navigateToWrittenPosts
        binding.onWrittenReviewMoreButtonClick = ::navigateToWrittenReviews
        binding.includeProfileReview.sliderReviewItemImages.setSliderAdapter(ReviewImageAdapter(), false)
    }

    private fun navigateToFollowers(userId: String) {
        findNavController().navigateSafely(
            ProfileFragmentDirections.actionProfileFragmentToFollowersFragment(userId)
        )
    }

    private fun navigateToFollowings(userId: String) {
        findNavController().navigateSafely(
            ProfileFragmentDirections.actionProfileFragmentToFollowingsFragment(userId)
        )
    }

    private fun handleFollowToggle() {
        if (profileViewModel.loginUser.value == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            profileViewModel.setFollow(!profileViewModel.isFollowing.value)
        }
    }

    private fun navigateToPostDetail(postId: String) {
        findNavController().navigateSafely(
            ProfileFragmentDirections.actionProfileFragmentToPostDetailFragment(postId)
        )
    }

    private fun navigateToWrittenPosts(userId: String) {
        findNavController().navigateSafely(
            ProfileFragmentDirections.actionProfileFragmentToWrittenPostsFragment(userId)
        )
    }

    private fun navigateToWrittenReviews(userId: String) {
        findNavController().navigateSafely(
            ProfileFragmentDirections.actionProfileFragmentToWrittenReviewsFragment(userId)
        )
    }

    private fun setupKidsRecyclerView() {
        binding.rvProfileKids.adapter = KidsAdapter(onKidAddClick = {})
    }

    private fun observeProfileUiEvent() {
        repeatOnStarted { profileViewModel.uiEvent.collect(::handleUiEvent) }
    }

    private fun handleUiEvent(uiEvent: ProfileUiEvent) {
        when (uiEvent) {
            ProfileUiEvent.LeavedUserFetch -> showLeavedUserDialog()
        }
    }

    private fun showLeavedUserDialog() {
        LeavedUserDialog().show(childFragmentManager, null)
    }

    companion object {

        const val KEY_USER_ID = "userId"
    }
}
