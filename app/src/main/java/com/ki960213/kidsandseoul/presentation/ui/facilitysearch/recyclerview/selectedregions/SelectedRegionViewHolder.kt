package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.selectedregions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemFilterSelectedRegionBinding
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate.RegionUiState

class SelectedRegionViewHolder(
    parent: ViewGroup,
    private val onBoroughClick: (boroughId: Long) -> Unit,
    private val onDongClick: (dongId: Long) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_filter_selected_region, parent, false)
) {

    private val binding = ItemFilterSelectedRegionBinding.bind(itemView)

    fun bind(selectedRegion: RegionUiState) {
        when (selectedRegion) {
            is RegionUiState.Borough -> {
                binding.regionId = selectedRegion.borough.id
                binding.regionName = "서울 ${selectedRegion.borough.name} 전체"
                binding.onRegionClick = onBoroughClick
            }

            is RegionUiState.Dong -> {
                binding.regionId = selectedRegion.administrativeDong.id
                binding.regionName = "서울 ${selectedRegion.administrativeDong.borough.name} ${selectedRegion.administrativeDong.name}"
                binding.onRegionClick = onDongClick
            }
        }
    }
}
