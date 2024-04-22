package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.presentation.common.base.InfoDialogFragment

class DeleteFetchDialog : InfoDialogFragment() {

    override val title: String
        get() = "삭제된 게시글"
    override val message: String
        get() = "해당 게시글은 삭제되었습니다."

    override fun action() {
        requireParentFragment().findNavController().popBackStack()
    }
}
