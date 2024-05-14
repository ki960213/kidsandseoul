package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.selectableregions

import androidx.recyclerview.widget.DiffUtil
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.RegionUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.SelectableRegionUiState

class SelectableRegionDiffUtil : DiffUtil.ItemCallback<SelectableRegionUiState>() {

    override fun areItemsTheSame(oldItem: SelectableRegionUiState, newItem: SelectableRegionUiState): Boolean =
        when {
            oldItem.region is RegionUiState.Borough && newItem.region is RegionUiState.Borough ->
                oldItem.region.borough.id == newItem.region.borough.id

            oldItem.region is RegionUiState.Dong && newItem.region is RegionUiState.Dong ->
                oldItem.region.administrativeDong.id == newItem.region.administrativeDong.id

            else -> false
        }

    override fun areContentsTheSame(oldItem: SelectableRegionUiState, newItem: SelectableRegionUiState): Boolean =
        oldItem == newItem
}
