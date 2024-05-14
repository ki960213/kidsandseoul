package com.ki960213.kidsandseoul.presentation.ui.writtenReviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentWrittenReviewsBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.reviews.ReviewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WrittenReviewsFragment : BaseFragment<FragmentWrittenReviewsBinding>(R.layout.fragment_written_reviews) {

    private val viewModel: WrittenReviewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupReviewsRecyclerView()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupReviewsRecyclerView() {
        binding.rvWrittenReviews.adapter = ReviewsAdapter(
            onProfileClick = ::navigateToProfile,
            onFacilityNameClick = ::navigateToFacilityDetail,
            onDeleteButtonClick = ::showDeleteConfirmDialog,
        )
    }

    private fun navigateToProfile(authorId: String) {
        val direction = WrittenReviewsFragmentDirections
            .actionWrittenReviewsFragmentToProfileFragment(authorId)
        findNavController().navigateSafely(direction)
    }

    private fun navigateToFacilityDetail(facilityId: Long) {
        val direction = WrittenReviewsFragmentDirections
            .actionWrittenReviewsFragmentToFacilityDetailFragment(facilityId)
        findNavController().navigateSafely(direction)
    }

    private fun showDeleteConfirmDialog(reviewId: String) {
        val dialog = ReviewDeleteConfirmDialog()
        dialog.arguments = ReviewDeleteConfirmDialog.createArgs(reviewId)
        dialog.show(childFragmentManager, null)
    }

    companion object {

        const val KEY_USER_ID = "userId"
    }
}
