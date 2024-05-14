package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.selectableregions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemFilterSelectableRegionBinding
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.RegionUiState
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.SelectableRegionUiState

class SelectableRegionViewHolder(
    parent: ViewGroup,
    private val onBoroughClick: (Long) -> Unit,
    private val onDongClick: (Long) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_filter_selectable_region, parent, false)
) {

    private val binding = ItemFilterSelectableRegionBinding.bind(itemView)

    fun bind(selectableRegion: SelectableRegionUiState) {
        when (val region = selectableRegion.region) {
            is RegionUiState.Borough -> {
                binding.onRegionClick = onBoroughClick
                binding.isSelected = selectableRegion.isSelected
                binding.regionId = region.borough.id
                binding.regionName = region.borough.name + " 전체"
            }

            is RegionUiState.Dong -> {
                binding.onRegionClick = onDongClick
                binding.isSelected = selectableRegion.isSelected
                binding.regionId = region.administrativeDong.id
                binding.regionName = region.administrativeDong.name
            }
        }
    }
}
