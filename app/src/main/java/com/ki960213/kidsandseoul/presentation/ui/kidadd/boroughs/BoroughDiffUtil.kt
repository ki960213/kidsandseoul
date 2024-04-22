package com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs

import androidx.recyclerview.widget.DiffUtil

class BoroughDiffUtil : DiffUtil.ItemCallback<BoroughUiState>() {

    override fun areItemsTheSame(oldItem: BoroughUiState, newItem: BoroughUiState): Boolean =
        oldItem.borough.id == newItem.borough.id

    override fun areContentsTheSame(oldItem: BoroughUiState, newItem: BoroughUiState): Boolean =
        oldItem == newItem
}
