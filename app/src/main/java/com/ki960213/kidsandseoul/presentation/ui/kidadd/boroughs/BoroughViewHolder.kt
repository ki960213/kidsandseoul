package com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemAllBoroughBinding

class BoroughViewHolder(
    parent: ViewGroup,
    onBoroughClick: (boroughId: Long) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_all_borough, parent, false)
) {

    private val binding = ItemAllBoroughBinding.bind(itemView)

    init {
        binding.onBoroughClick = onBoroughClick
    }

    fun bind(uiState: BoroughUiState) {
        binding.uiState = uiState
    }
}
