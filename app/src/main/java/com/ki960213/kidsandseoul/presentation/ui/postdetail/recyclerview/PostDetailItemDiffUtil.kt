package com.ki960213.kidsandseoul.presentation.ui.postdetail.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.CommentUiState
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailItemUiState
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailUiState

class PostDetailItemDiffUtil : DiffUtil.ItemCallback<PostDetailItemUiState>() {

    override fun areItemsTheSame(
        oldItem: PostDetailItemUiState,
        newItem: PostDetailItemUiState,
    ): Boolean = when {
        oldItem is PostDetailUiState && newItem is PostDetailUiState ->
            oldItem.post.id == newItem.post.id

        oldItem is CommentUiState && newItem is CommentUiState ->
            oldItem.comment.id == newItem.comment.id

        else -> false
    }

    override fun areContentsTheSame(
        oldItem: PostDetailItemUiState,
        newItem: PostDetailItemUiState,
    ): Boolean = oldItem == newItem
}
