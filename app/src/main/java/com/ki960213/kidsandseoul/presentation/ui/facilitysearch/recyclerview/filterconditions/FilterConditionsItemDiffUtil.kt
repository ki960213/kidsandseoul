package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.filterconditions

import androidx.recyclerview.widget.DiffUtil

class FilterConditionsItemDiffUtil : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}
