package com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class BoroughsAdapter(
    private val onBoroughClick: (boroughId: Long) -> Unit,
) : ListAdapter<BoroughUiState, BoroughViewHolder>(BoroughDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoroughViewHolder =
        BoroughViewHolder(parent, onBoroughClick)

    override fun onBindViewHolder(holder: BoroughViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
