package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class ChatRoomsAdapter(
    private val onChatRoomClick: (chatRoomId: String) -> Unit,
    private val onNotificationToggleClick: (chatRoomId: String, turnOn: Boolean) -> Unit,
) : ListAdapter<ChatRoomUiState, ChatRoomViewHolder>(ChatRoomUiStateDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder =
        ChatRoomViewHolder(parent, onChatRoomClick, onNotificationToggleClick)

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
