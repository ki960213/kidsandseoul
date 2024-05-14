package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.reviews

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment

class ReviewDeleteConfirmDialog : ConfirmDialogFragment() {

    private val viewModel: ReviewsViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val title: String
        get() = "리뷰 삭제"
    override val message: String
        get() = "리뷰를 삭제하시겠습니까?"

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
