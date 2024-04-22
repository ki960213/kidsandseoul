package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment
import com.ki960213.kidsandseoul.presentation.ui.postdetail.PostDetailViewModel

class DeleteCommentConfirmDialog : ConfirmDialogFragment() {

    private val viewModel: PostDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val title: String
        get() = "댓글 삭제"
    override val message: String
        get() = "해당 댓글을 삭제하시겠습니까?"

    override fun action() {
        val commentId = arguments?.getString(KEY_COMMENT_ID) ?: return
        viewModel.deleteComment(commentId)
    }

    companion object {

        private const val KEY_COMMENT_ID = "commentId"

        fun createArgs(commendId: String): Bundle = Bundle().apply {
            putString(KEY_COMMENT_ID, commendId)
        }
    }
}
