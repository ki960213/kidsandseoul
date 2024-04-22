package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemPostBinding

class PostViewHolder(
    parent: ViewGroup,
    onPostClick: (postId: String) -> Unit,
    onProfileUiClick: (userId: String) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
) {

    private val binding = ItemPostBinding.bind(itemView)

    init {
        binding.onPostClick = onPostClick
        binding.onProfileUiClick = onProfileUiClick
    }

    fun bind(uiState: PostUiState) {
        binding.uiState = uiState
    }
}
