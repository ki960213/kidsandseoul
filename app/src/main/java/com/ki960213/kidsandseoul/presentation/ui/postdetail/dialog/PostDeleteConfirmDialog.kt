package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment
import com.ki960213.kidsandseoul.presentation.ui.postdetail.PostDetailViewModel

class PostDeleteConfirmDialog : ConfirmDialogFragment() {

    private val viewModel: PostDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val title: String
        get() = "게시글 삭제"
    override val message: String
        get() = "게시글을 삭제하시겠습니까?"

    override fun action() {
        viewModel.deletePost()
        requireParentFragment().findNavController().popBackStack()
    }
}
