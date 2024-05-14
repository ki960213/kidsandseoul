package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.filterconditions

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class FilterConditionsAdapter :
    ListAdapter<String, FilterConditionsItemViewHolder>(FilterConditionsItemDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FilterConditionsItemViewHolder = FilterConditionsItemViewHolder(parent)

    override fun onBindViewHolder(holder: FilterConditionsItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}




