package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.facilities

import androidx.recyclerview.widget.DiffUtil
import com.ki960213.domain.facility.model.Facility

class FacilityDiffUtil : DiffUtil.ItemCallback<Facility>() {

    override fun areItemsTheSame(oldItem: Facility, newItem: Facility): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Facility, newItem: Facility): Boolean =
        oldItem == newItem
}
