package com.ki960213.kidsandseoul.presentation.ui.postwrite

import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment

class PostWriteExitConfirmDialog : ConfirmDialogFragment() {

    override val title: String
        get() = getString(R.string.post_write_exit_confirm_dialog_title)
    override val message: String
        get() = getString(R.string.post_write_exit_confirm_dialog_message)

    override fun action() {
        requireParentFragment().findNavController().popBackStack()
    }
}
