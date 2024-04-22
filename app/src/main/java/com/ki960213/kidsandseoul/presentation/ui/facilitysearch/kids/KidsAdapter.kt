package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.kids

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.domain.kid.model.Kid

class KidsAdapter(
    private val onKidClick: (kidId: String) -> Unit,
) : ListAdapter<Kid, KidViewHolder>(KidDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KidViewHolder =
        KidViewHolder(parent, onKidClick)

    override fun onBindViewHolder(holder: KidViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
