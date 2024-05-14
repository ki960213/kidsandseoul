package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.InfoDialogFragment

class ReportCompleteDialog : InfoDialogFragment() {

    override val title: String
        get() = getString(R.string.report_complete_dialog_title)
    override val message: String
        get() = getString(R.string.report_complete_dialog_message)
}
