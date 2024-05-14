package com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs

import androidx.recyclerview.widget.DiffUtil

class AdministrativeDongDiffUtil : DiffUtil.ItemCallback<AdministrativeDongUiState>() {

    override fun areItemsTheSame(
        oldItem: AdministrativeDongUiState,
        newItem: AdministrativeDongUiState,
    ): Boolean = oldItem.administrativeDong.id == newItem.administrativeDong.id

    override fun areContentsTheSame(
        oldItem: AdministrativeDongUiState,
        newItem: AdministrativeDongUiState,
    ): Boolean = oldItem == newItem
}
