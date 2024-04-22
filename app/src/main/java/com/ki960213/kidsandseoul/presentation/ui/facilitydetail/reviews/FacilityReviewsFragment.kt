package com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews

import android.os.Bundle
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilityReviewsBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.FacilityDetailFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.FacilityDetailViewModel
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.reviews.ReviewUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.reviews.ReviewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FacilityReviewsFragment :
    BaseFragment<FragmentFacilityReviewsBinding>(R.layout.fragment_facility_reviews) {

    private val viewModel: FacilityDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

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
            onFacilityNameClick = {},
            onDeleteButtonClick = ::showDeleteConfirmDialog,
        )
    }

    private fun navigateToProfile(authorId: String) {
        val direction = FacilityDetailFragmentDirections
            .actionFacilityDetailFragmentToProfileFragment(authorId)
        findNavController().navigateSafely(direction)
    }

    private fun showDeleteConfirmDialog(reviewId: String) {
        val dialog = ReviewDeleteConfirmDialog()
        dialog.arguments = ReviewDeleteConfirmDialog.createArgs(reviewId)
        dialog.show(parentFragmentManager, null)
    }
}

@BindingAdapter("app:all_reviews")
fun RecyclerView.setReviews(uiStates: List<ReviewUiState>) {
    (adapter as ReviewsAdapter).submitList(uiStates)
}
