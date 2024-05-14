package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.filterconditions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemFilterConditionsBinding

class FilterConditionsItemViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_filter_conditions, parent, false)
) {

    private val binding = ItemFilterConditionsBinding.bind(itemView)

    fun bind(text: String) {
        binding.tvFilterConditionsItem.text = text
    }
}
