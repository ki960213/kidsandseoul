package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.facilities

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.domain.facility.model.Facility

class FacilitiesAdapter1(
    private val onFacilityClick: (facilityId: Long) -> Unit,
) : PagingDataAdapter<Facility, FacilityViewHolder>(FacilityDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder =
        FacilityViewHolder(parent = parent, onFacilityClick = onFacilityClick)

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }
}

class FacilitiesAdapter(
    private val onFacilityClick: (facilityId: Long) -> Unit,
) : ListAdapter<Facility, FacilityViewHolder>(FacilityDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder =
        FacilityViewHolder(parent = parent, onFacilityClick = onFacilityClick)

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }
}
