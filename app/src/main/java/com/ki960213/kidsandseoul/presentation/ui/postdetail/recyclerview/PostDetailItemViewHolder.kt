package com.ki960213.kidsandseoul.presentation.ui.postdetail.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailItemUiState

sealed class PostDetailItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(uiState: PostDetailItemUiState)
}
