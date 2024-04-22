package com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemChatRoomOtherTextMessageBinding
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MessageUiState
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.OtherTextMessageUiState

class OtherTextMessageViewHolder(
    parent: ViewGroup,
    onProfileClick: (authorId: String) -> Unit,
    onMessageLongClick: () -> Boolean,
) : MessageViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_chat_room_other_text_message, parent, false)
) {

    private val binding = ItemChatRoomOtherTextMessageBinding.bind(itemView)

    init {
        binding.onProfileClick = onProfileClick
        binding.onMessageLongClick = onMessageLongClick
    }

    override fun bind(uiState: MessageUiState) {
        if (uiState !is OtherTextMessageUiState) return
        binding.uiState = uiState
    }
}
