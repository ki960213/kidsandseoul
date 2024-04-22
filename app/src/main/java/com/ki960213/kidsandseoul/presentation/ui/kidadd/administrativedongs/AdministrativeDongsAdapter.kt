package com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class AdministrativeDongsAdapter(
    private val onAdministrativeDongClick: (administrativeDongId: Long) -> Unit,
) : ListAdapter<AdministrativeDongUiState, AdministrativeDongViewHolder>(AdministrativeDongDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdministrativeDongViewHolder =
        AdministrativeDongViewHolder(parent, onAdministrativeDongClick)

    override fun onBindViewHolder(holder: AdministrativeDongViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
