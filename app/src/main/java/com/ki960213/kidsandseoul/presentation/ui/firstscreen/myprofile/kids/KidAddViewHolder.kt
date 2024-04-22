package com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemProfileKidAddBinding

class KidAddViewHolder(
    parent: ViewGroup,
    onKidAddClick: () -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_profile_kid_add, parent, false)
) {

    private val binding = ItemProfileKidAddBinding.bind(itemView)

    init {
        binding.onKidAddButtonClick = onKidAddClick
    }
}
