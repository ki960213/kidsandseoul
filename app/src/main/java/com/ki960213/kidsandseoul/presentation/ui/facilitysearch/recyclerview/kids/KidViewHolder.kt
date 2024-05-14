package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.recyclerview.kids

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.kid.model.Kid
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemFilterKidBinding

class KidViewHolder(
    parent: ViewGroup,
    onKidClick: (kidId: String) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_filter_kid, parent, false)
) {

    private val binding = ItemFilterKidBinding.bind(itemView)

    init {
        binding.onKidClick = onKidClick
    }

    fun bind(kid: Kid) {
        binding.kid = kid
    }
}
