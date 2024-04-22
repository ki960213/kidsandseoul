package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.filtercondition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemFilterConditionBinding

class FilterConditionItemViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_filter_condition, parent, false)
) {

    private val binding = ItemFilterConditionBinding.bind(itemView)

    fun bind(text: String) {
        binding.tvFilterConditionItem.text = text
    }
}
