package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms

import androidx.recyclerview.widget.DiffUtil

class ChatRoomUiStateDiffUtil : DiffUtil.ItemCallback<ChatRoomUiState>() {

    override fun areItemsTheSame(oldItem: ChatRoomUiState, newItem: ChatRoomUiState): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: ChatRoomUiState, newItem: ChatRoomUiState): Boolean =
        oldItem == newItem
}
