package com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemChatRoomMyTextMessageBinding
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MessageUiState
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MyTextMessageUiState

class MyTextMessageViewHolder(
    parent: ViewGroup,
) : MessageViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_chat_room_my_text_message, parent, false)
) {

    private val binding = ItemChatRoomMyTextMessageBinding.bind(itemView)

    override fun bind(uiState: MessageUiState) {
        if (uiState !is MyTextMessageUiState) return
        binding.uiState = uiState
    }
}
