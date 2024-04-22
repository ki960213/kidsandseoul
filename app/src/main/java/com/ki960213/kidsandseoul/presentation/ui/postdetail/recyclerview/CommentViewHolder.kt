package com.ki960213.kidsandseoul.presentation.ui.postdetail.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemCommentBinding
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.CommentUiState
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailItemUiState

class CommentViewHolder(
    parent: ViewGroup,
    onProfileUiClick: (authorId: String) -> Unit,
    onDeleteButtonClick: (commentId: String) -> Unit,
    onChildCommentButtonClick: (commentId: String) -> Unit,
) : PostDetailItemViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
) {

    private val binding = ItemCommentBinding.bind(itemView)

    init {
        binding.onProfileUiClick = onProfileUiClick
        binding.onDeleteButtonClick = onDeleteButtonClick
        binding.onChildCommentsButtonClick = onChildCommentButtonClick
    }

    override fun bind(uiState: PostDetailItemUiState) {
        if (uiState !is CommentUiState) return
        binding.uiState = uiState
    }
}
