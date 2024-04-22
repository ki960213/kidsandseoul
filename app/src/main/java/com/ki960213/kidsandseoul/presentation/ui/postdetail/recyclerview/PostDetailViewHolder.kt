package com.ki960213.kidsandseoul.presentation.ui.postdetail.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemPostDetailBinding
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailItemUiState
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailUiState

class PostDetailViewHolder(
    parent: ViewGroup,
    onProfileUiClick: (authorId: String) -> Unit,
    onLikeToggle: (postId: String, willLike: Boolean) -> Unit,
) : PostDetailItemViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_post_detail, parent, false)
) {

    private val binding = ItemPostDetailBinding.bind(itemView)

    init {
        binding.onProfileUiClick = onProfileUiClick
        binding.onLikeToggle = onLikeToggle
    }

    override fun bind(uiState: PostDetailItemUiState) {
        if (uiState !is PostDetailUiState) return
        binding.uiState = uiState
    }
}
