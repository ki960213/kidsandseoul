package com.ki960213.kidsandseoul.presentation.ui

import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.InfoDialogFragment

class NotOnlineDialog : InfoDialogFragment() {

    override val title: String
        get() = getString(R.string.not_online_dialog_title)
    override val message: String
        get() = getString(R.string.not_online_dialog_message)

    override fun action() {
        requireActivity().finishAffinity()
    }

    companion object {
        const val TAG = "NotOnlineDialog"
    }
}
