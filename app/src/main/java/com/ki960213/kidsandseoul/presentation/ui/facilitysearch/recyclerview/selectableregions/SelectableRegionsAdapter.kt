package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.selectableregions

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.SelectableRegionUiState

class SelectableRegionsAdapter(
    private val onBoroughClick: (boroughId: Long) -> Unit,
    private val onDongClick: (dongId: Long) -> Unit,
) : ListAdapter<SelectableRegionUiState, SelectableRegionViewHolder>(SelectableRegionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableRegionViewHolder =
        SelectableRegionViewHolder(parent, onBoroughClick, onDongClick)

    override fun onBindViewHolder(holder: SelectableRegionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
