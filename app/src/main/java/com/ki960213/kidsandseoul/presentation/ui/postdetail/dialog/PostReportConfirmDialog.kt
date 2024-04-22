package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment
import com.ki960213.kidsandseoul.presentation.ui.chatroom.dialog.ReportCompleteDialog

class PostReportConfirmDialog : ConfirmDialogFragment() {

    override val title: String
        get() = "게시글 신고"
    override val message: String
        get() = "게시글을 신고하시겠습니까?"

    override fun action() {
        ReportCompleteDialog().show(parentFragmentManager, null)
    }
}
