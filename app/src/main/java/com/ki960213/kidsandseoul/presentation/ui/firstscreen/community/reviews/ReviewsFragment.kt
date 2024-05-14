package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentReviewsBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.MainActivity
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.reviews.ReviewsAdapter
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.FirstScreenFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : BaseFragment<FragmentReviewsBinding>(R.layout.fragment_reviews) {

    private val viewModel: ReviewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupReviewsRecyclerView()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
    }

    private fun setupReviewsRecyclerView() {
        binding.rvFacilityReviews.adapter = ReviewsAdapter(
            onProfileClick = ::navigateToProfile,
            onFacilityNameClick = ::navigateToFacilityDetail,
            onDeleteButtonClick = ::showDeleteConfirmDialog,
        )
    }

    private fun navigateToProfile(authorId: String) {
        val direction = FirstScreenFragmentDirections
            .actionFirstScreenFragmentToProfileFragment(authorId)
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun navigateToFacilityDetail(facilityId: Long) {
        val direction = FirstScreenFragmentDirections
            .actionFirstScreenFragmentToFacilityDetailFragment(facilityId)
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun showDeleteConfirmDialog(reviewId: String) {
        val dialog = ReviewDeleteConfirmDialog()
        dialog.arguments = ReviewDeleteConfirmDialog.createArgs(reviewId)
        dialog.show(parentFragmentManager, null)
    }
}
