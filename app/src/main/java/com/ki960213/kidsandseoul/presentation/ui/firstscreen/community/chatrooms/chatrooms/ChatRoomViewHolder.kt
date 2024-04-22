package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.chat.model.ImageMessage
import com.ki960213.domain.chat.model.InOutMessage
import com.ki960213.domain.chat.model.Message
import com.ki960213.domain.chat.model.TextMessage
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemChatRoomsBinding

class ChatRoomViewHolder(
    parent: ViewGroup,
    onChatRoomClick: (chatRoomId: String) -> Unit,
    onNotificationToggleClick: (chatRoomId: String, turnOn: Boolean) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_chat_rooms, parent, false)
) {

    private val binding = ItemChatRoomsBinding.bind(itemView)

    init {
        binding.onChatRoomClick = onChatRoomClick
        binding.onNotificationToggleClick = onNotificationToggleClick
    }

    fun bind(uiState: ChatRoomUiState) {
        binding.uiState = uiState
    }
}

@BindingAdapter("app:chatRoomItem_message")
fun TextView.setMessage(message: Message) {
    text = when (message) {
        is ImageMessage -> "사진"
        is InOutMessage -> throw AssertionError("최근 메세지는 텍스트나 이미지 메세지만 저장됨")
        is TextMessage -> message.text
    }
}
