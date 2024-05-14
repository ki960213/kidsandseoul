package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.selectedregions

import androidx.recyclerview.widget.DiffUtil
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.RegionUiState

class RegionDiffUtil : DiffUtil.ItemCallback<RegionUiState>() {

    override fun areItemsTheSame(oldItem: RegionUiState, newItem: RegionUiState): Boolean =
        when {
            oldItem is RegionUiState.Borough && newItem is RegionUiState.Borough ->
                oldItem.borough.id == newItem.borough.id

            oldItem is RegionUiState.Dong && newItem is RegionUiState.Dong ->
                oldItem.administrativeDong.id == newItem.administrativeDong.id

            else -> false
        }

    override fun areContentsTheSame(oldItem: RegionUiState, newItem: RegionUiState): Boolean =
        oldItem == newItem
}
