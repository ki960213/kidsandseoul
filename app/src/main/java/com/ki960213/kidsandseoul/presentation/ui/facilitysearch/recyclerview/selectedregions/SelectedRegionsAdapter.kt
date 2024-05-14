package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.selectedregions

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.RegionUiState

class SelectedRegionsAdapter(
    private val onBoroughClick: (boroughId: Long) -> Unit,
    private val onDongClick: (dongId: Long) -> Unit,
) : ListAdapter<RegionUiState, SelectedRegionViewHolder>(RegionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedRegionViewHolder =
        SelectedRegionViewHolder(parent, onBoroughClick, onDongClick)

    override fun onBindViewHolder(holder: SelectedRegionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
