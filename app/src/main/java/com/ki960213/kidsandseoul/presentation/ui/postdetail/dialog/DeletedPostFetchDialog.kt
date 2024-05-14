package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.InfoDialogFragment

class DeletedPostFetchDialog : InfoDialogFragment() {

    override val title: String
        get() = getString(R.string.deleted_post_fetch_dialog_title)
    override val message: String
        get() = getString(R.string.deleted_post_fetch_dialog_message)

    override fun action() {
        requireParentFragment().findNavController().popBackStack()
    }
}
