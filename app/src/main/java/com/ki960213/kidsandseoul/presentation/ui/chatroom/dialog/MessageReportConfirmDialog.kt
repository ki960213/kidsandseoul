package com.ki960213.kidsandseoul.presentation.ui.chatroom.dialog

import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment

class MessageReportConfirmDialog : ConfirmDialogFragment() {

    override val title: String
        get() = "메세지 신고"
    override val message: String
        get() = "메세지를 신고하시겠습니까?"

    override fun action() {
        ReportCompleteDialog().show(parentFragmentManager, null)
    }
}
