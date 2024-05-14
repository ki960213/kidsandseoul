package com.ki960213.kidsandseoul.presentation.ui.profileedit.kids

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class KidsAdapter(
    private val onKidDeleteClick: (kidId: String) -> Unit,
    private val onKidAddClick: () -> Unit,
) : ListAdapter<KidsItemUiState, RecyclerView.ViewHolder>(KidsItemUiStateDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = when (viewType) {
        KID_ADD_VIEW_TYPE -> KidAddViewHolder(parent, onKidAddClick)
        KID_VIEW_TYPE -> KidViewHolder(parent, onKidDeleteClick)
        else -> throw AssertionError("KidsAdapter의 뷰타입 잘못 구현됨.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == KID_VIEW_TYPE) {
            (holder as KidViewHolder).bind(getItem(position) as KidUiState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is KidAddUiState) KID_ADD_VIEW_TYPE else KID_VIEW_TYPE
    }

    companion object {

        private const val KID_ADD_VIEW_TYPE = 0
        private const val KID_VIEW_TYPE = 1
    }
}
