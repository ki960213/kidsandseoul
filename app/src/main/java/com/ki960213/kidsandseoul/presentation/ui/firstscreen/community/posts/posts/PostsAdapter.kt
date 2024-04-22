package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class PostsAdapter(
    private val onPostClick: (postId: String) -> Unit,
    private val onProfileUiClick: (userId: String) -> Unit,
) : ListAdapter<PostUiState, PostViewHolder>(PostDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            parent = parent,
            onPostClick = onPostClick,
            onProfileUiClick = onProfileUiClick,
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
