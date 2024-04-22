package com.ki960213.kidsandseoul.presentation.ui.followers.users

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class FollowUsersAdapter(
    private val onUserClick: (userId: String) -> Unit,
    private val onFollowToggle: (userId: String, willFollow: Boolean) -> Unit,
) : ListAdapter<FollowUserUiState, FollowUserViewHolder>(FollowUserDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowUserViewHolder =
        FollowUserViewHolder(
            parent = parent,
            onUserClick = onUserClick,
            onFollowToggle = onFollowToggle,
        )

    override fun onBindViewHolder(holder: FollowUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
