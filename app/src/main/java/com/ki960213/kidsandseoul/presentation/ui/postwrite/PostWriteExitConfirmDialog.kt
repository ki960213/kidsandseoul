package com.ki960213.kidsandseoul.presentation.ui.postwrite

import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment

class PostWriteExitConfirmDialog : ConfirmDialogFragment() {

    override val title: String
        get() = "게시글 등록 취소"
    override val message: String
        get() = "작성을 종료할까요?\n작성중이던 게시글은 저장되지 않아요."

    override fun action() {
        requireParentFragment().findNavController().popBackStack()
    }
}
