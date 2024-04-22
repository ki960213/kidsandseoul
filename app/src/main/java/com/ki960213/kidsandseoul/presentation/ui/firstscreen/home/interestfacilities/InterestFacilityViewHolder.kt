package com.ki960213.kidsandseoul.presentation.ui.firstscreen.home.interestfacilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.facility.model.Facility
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemInterestFacilityBinding

class InterestFacilityViewHolder(
    parent: ViewGroup,
    onFacilityClick: (facilityId: Long) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_interest_facility, parent, false)
) {

    private val binding = ItemInterestFacilityBinding.bind(itemView)

    init {
        binding.onFacilityClick = onFacilityClick
    }

    fun bind(facility: Facility) {
        binding.facility = facility
    }
}
