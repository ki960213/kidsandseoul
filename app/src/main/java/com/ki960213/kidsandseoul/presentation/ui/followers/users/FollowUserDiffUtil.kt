package com.ki960213.kidsandseoul.presentation.ui.followers.users

import androidx.recyclerview.widget.DiffUtil

class FollowUserDiffUtil : DiffUtil.ItemCallback<FollowUserUiState>() {

    override fun areItemsTheSame(oldItem: FollowUserUiState, newItem: FollowUserUiState): Boolean =
        oldItem.user.id == newItem.user.id

    override fun areContentsTheSame(
        oldItem: FollowUserUiState,
        newItem: FollowUserUiState,
    ): Boolean = oldItem == newItem
}
