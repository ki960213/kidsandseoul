package com.ki960213.kidsandseoul.presentation.ui.firstscreen.home.interestfacilities

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.domain.facility.model.Facility

class InterestFacilitiesAdapter(
    private val onFacilityClick: (facilityId: Long) -> Unit,
) : ListAdapter<Facility, InterestFacilityViewHolder>(FacilityDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestFacilityViewHolder =
        InterestFacilityViewHolder(parent, onFacilityClick)

    override fun onBindViewHolder(holder: InterestFacilityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
