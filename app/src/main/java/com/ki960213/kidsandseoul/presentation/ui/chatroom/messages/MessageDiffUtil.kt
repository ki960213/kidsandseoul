package com.ki960213.kidsandseoul.presentation.ui.chatroom.messages

import androidx.recyclerview.widget.DiffUtil

class MessageDiffUtil : DiffUtil.ItemCallback<MessageUiState>() {

    override fun areItemsTheSame(oldItem: MessageUiState, newItem: MessageUiState): Boolean =
        oldItem.createdAt == newItem.createdAt

    override fun areContentsTheSame(oldItem: MessageUiState, newItem: MessageUiState): Boolean =
        oldItem == newItem
}
