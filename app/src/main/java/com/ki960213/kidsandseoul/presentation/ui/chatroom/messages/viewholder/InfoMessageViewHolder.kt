package com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemChatRoomInfoMessageBinding
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.DateUiState
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.InOutUiState
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MessageUiState
import java.time.format.DateTimeFormatter

class InfoMessageViewHolder(
    parent: ViewGroup,
) : MessageViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room_info_message, parent, false)
) {

    private val binding = ItemChatRoomInfoMessageBinding.bind(itemView)

    @SuppressLint("SetTextI18n")
    override fun bind(uiState: MessageUiState) {
        when (uiState) {
            is DateUiState -> {
                binding.tvChatRoomItemInfo.text =
                    uiState.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. EEEE"))
            }

            is InOutUiState -> {
                binding.tvChatRoomItemInfo.text =
                    "${uiState.authorName}님이 ${if (uiState.isIn) "들어왔습니다." else "나갔습니다."}"
            }

            else -> {}
        }
    }
}
