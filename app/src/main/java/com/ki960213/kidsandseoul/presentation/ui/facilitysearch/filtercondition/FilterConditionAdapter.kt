package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.filtercondition

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class FilterConditionAdapter :
    ListAdapter<String, FilterConditionItemViewHolder>(FilterConditionItemDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FilterConditionItemViewHolder = FilterConditionItemViewHolder(parent)

    override fun onBindViewHolder(holder: FilterConditionItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}




