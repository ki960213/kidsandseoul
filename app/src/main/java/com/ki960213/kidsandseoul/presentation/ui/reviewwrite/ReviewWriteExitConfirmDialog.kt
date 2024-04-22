package com.ki960213.kidsandseoul.presentation.ui.reviewwrite

import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment

class ReviewWriteExitConfirmDialog : ConfirmDialogFragment() {

    override val title: String
        get() = "리뷰 등록 취소"
    override val message: String
        get() = "작성을 종료할까요?\n작성중이던 리뷰는 저장되지 않아요."

    override fun action() {
        requireParentFragment().findNavController().popBackStack()
    }
}
