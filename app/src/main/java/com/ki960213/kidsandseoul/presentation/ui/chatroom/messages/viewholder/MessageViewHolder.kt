package com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MessageUiState

abstract class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(uiState: MessageUiState)
}
