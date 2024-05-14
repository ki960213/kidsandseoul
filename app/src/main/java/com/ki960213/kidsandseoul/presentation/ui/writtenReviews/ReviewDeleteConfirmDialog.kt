package com.ki960213.kidsandseoul.presentation.ui.writtenReviews

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment

class ReviewDeleteConfirmDialog : ConfirmDialogFragment() {

    private val viewModel: WrittenReviewsViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val title: String
        get() = getString(R.string.review_delete_confirm_dialog_title)
    override val message: String
        get() = getString(R.string.review_delete_confirm_dialog_message)

    override fun action() {
        val reviewId = arguments?.getString(KEY_REVIEW_ID) ?: return
        viewModel.deleteReview(reviewId)
    }

    companion object {

        private const val KEY_REVIEW_ID = "reviewId"

        fun createArgs(reviewId: String): Bundle = Bundle().apply {
            putString(KEY_REVIEW_ID, reviewId)
        }
    }
}
