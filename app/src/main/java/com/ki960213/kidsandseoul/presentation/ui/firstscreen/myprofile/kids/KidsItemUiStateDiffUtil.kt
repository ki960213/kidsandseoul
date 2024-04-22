package com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids

import androidx.recyclerview.widget.DiffUtil

class KidsItemUiStateDiffUtil : DiffUtil.ItemCallback<KidsItemUiState>() {

    override fun areItemsTheSame(oldItem: KidsItemUiState, newItem: KidsItemUiState): Boolean {
        if (oldItem is KidAddUiState && newItem is KidAddUiState) return true
        if (oldItem !is KidUiState) return false
        if (newItem !is KidUiState) return false
        return oldItem.kid.id == newItem.kid.id
    }

    override fun areContentsTheSame(oldItem: KidsItemUiState, newItem: KidsItemUiState): Boolean =
        oldItem == newItem
}
