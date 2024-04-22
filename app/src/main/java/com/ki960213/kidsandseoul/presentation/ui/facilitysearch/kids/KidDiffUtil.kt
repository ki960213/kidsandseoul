package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.kids

import androidx.recyclerview.widget.DiffUtil
import com.ki960213.domain.kid.model.Kid

class KidDiffUtil : DiffUtil.ItemCallback<Kid>() {

    override fun areItemsTheSame(oldItem: Kid, newItem: Kid): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Kid, newItem: Kid): Boolean =
        oldItem == newItem
}
