package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment

class PostReportConfirmDialog : ConfirmDialogFragment() {

    override val title: String
        get() = getString(R.string.post_report_confirm_dialog_title)
    override val message: String
        get() = getString(R.string.post_report_confirm_dialog_message)

    override fun action() {
        ReportCompleteDialog().show(parentFragmentManager, null)
    }
}
