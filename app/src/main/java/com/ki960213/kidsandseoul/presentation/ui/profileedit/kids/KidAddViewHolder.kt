package com.ki960213.kidsandseoul.presentation.ui.profileedit.kids

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemProfileEditKidAddBinding

class KidAddViewHolder(
    parent: ViewGroup,
    onKidAddClick: () -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_profile_edit_kid_add, parent, false)
) {

    private val binding = ItemProfileEditKidAddBinding.bind(itemView)

    init {
        binding.onKidAddButtonClick = onKidAddClick
    }
}
