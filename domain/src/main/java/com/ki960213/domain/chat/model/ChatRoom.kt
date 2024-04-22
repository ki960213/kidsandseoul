package com.ki960213.domain.chat.model

sealed interface ChatRoom {

    val id: String
    val name: String
    val unreadMessageCount: Int
    val recentMessage: Message
    val isNotificationTurnOn: Boolean
}





