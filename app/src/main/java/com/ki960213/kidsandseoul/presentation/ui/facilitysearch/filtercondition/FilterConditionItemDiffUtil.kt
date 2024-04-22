package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.filtercondition

import androidx.recyclerview.widget.DiffUtil

class FilterConditionItemDiffUtil : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}
