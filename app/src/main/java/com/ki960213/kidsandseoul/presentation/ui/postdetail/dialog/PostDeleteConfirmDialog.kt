package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment
import com.ki960213.kidsandseoul.presentation.ui.postdetail.PostDetailViewModel

class PostDeleteConfirmDialog : ConfirmDialogFragment() {

    private val viewModel: PostDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val title: String
        get() = getString(R.string.post_delete_comfirm_dialog_title)
    override val message: String
        get() = getString(R.string.post_delete_comfirm_dialog_message)

    override fun action() {
        viewModel.deletePost()
        requireParentFragment().findNavController().popBackStack()
    }
}
