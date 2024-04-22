package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts

import androidx.recyclerview.widget.DiffUtil

class PostDiffUtil : DiffUtil.ItemCallback<PostUiState>() {

    override fun areItemsTheSame(oldItem: PostUiState, newItem: PostUiState): Boolean =
        oldItem.post.id == newItem.post.id

    override fun areContentsTheSame(oldItem: PostUiState, newItem: PostUiState): Boolean =
        oldItem == newItem
}
