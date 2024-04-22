package com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemChatRoomOtherImageMessageBinding
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MessageUiState
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.OtherImageMessageUiState

class OtherImageMessageViewHolder(
    parent: ViewGroup,
    onProfileClick: (authorId: String) -> Unit,
    onMessageLongClick: () -> Boolean,
) : MessageViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_chat_room_other_image_message, parent, false)
) {

    private val binding = ItemChatRoomOtherImageMessageBinding.bind(itemView)

    init {
        binding.onProfileClick = onProfileClick
        binding.onMessageLongClick = onMessageLongClick
    }

    override fun bind(uiState: MessageUiState) {
        if (uiState !is OtherImageMessageUiState) return
        binding.uiState = uiState
    }
}
