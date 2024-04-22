package com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemProfileKidBinding

class KidViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_profile_kid, parent, false)
) {

    private val binding = ItemProfileKidBinding.bind(itemView)

    fun bind(uiState: KidUiState) {
        binding.uiState = uiState
    }
}
