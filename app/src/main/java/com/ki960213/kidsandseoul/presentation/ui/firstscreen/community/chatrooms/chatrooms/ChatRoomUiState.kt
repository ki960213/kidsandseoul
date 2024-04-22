package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms

import com.ki960213.domain.chat.model.ChatRoom
import com.ki960213.domain.chat.model.FacilityChatRoom
import com.ki960213.domain.chat.model.PrivateChatRoom

sealed interface ChatRoomUiState {

    val chatRoom: ChatRoom
}

data class FacilityChatRoomUiState(
    override val chatRoom: FacilityChatRoom,
) : ChatRoomUiState

data class PrivateChatRoomUiState(
    val interlocutorImageUrl: String,
    override val chatRoom: PrivateChatRoom,
) : ChatRoomUiState
