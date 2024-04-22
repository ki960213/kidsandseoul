package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.facilities

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.domain.facility.model.Facility

class FacilitiesAdapter(
    private val onFacilityClick: (facilityId: Long) -> Unit,
) : ListAdapter<Facility, FacilityViewHolder>(FacilityDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder =
        FacilityViewHolder(parent = parent, onFacilityClick = onFacilityClick)

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
