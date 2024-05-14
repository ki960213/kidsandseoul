package com.ki960213.kidsandseoul.presentation.ui.profile

import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.InfoDialogFragment

class LeavedUserDialog : InfoDialogFragment() {

    override val title: String
        get() = getString(R.string.leaved_user_dialog_title)
    override val message: String
        get() = getString(R.string.leaved_user_dialog_message)

    override fun action() {
        requireParentFragment().findNavController().popBackStack()
    }
}
