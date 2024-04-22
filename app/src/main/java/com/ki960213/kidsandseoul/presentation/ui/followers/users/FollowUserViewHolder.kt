package com.ki960213.kidsandseoul.presentation.ui.followers.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemAllFollowUserBinding

class FollowUserViewHolder(
    parent: ViewGroup,
    onUserClick: (userId: String) -> Unit,
    onFollowToggle: (userId: String, willFollow: Boolean) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_all_follow_user, parent, false)
) {

    private val binding = ItemAllFollowUserBinding.bind(itemView)

    init {
        binding.onUserClick = onUserClick
        binding.onFollowToggle = onFollowToggle
    }

    fun bind(uiState: FollowUserUiState) {
        binding.uiState = uiState
    }
}
