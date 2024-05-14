package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.facilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.facility.model.Facility
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemFacilityBinding

class FacilityViewHolder(
    parent: ViewGroup,
    onFacilityClick: (facilityId: Long) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_facility, parent, false)
) {

    val binding = ItemFacilityBinding.bind(itemView)

    init {
        binding.onFacilityClick = onFacilityClick
    }

    fun bind(facility: Facility) {
        binding.facility = facility
    }
}
