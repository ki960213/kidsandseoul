package com.ki960213.domain.chat.model

data class FacilityChatRoom(
    override val id: String,
    override val name: String,
    override val unreadMessageCount: Int,
    override val recentMessage: Message,
    override val isNotificationTurnOn: Boolean,
    val memberIds: List<String>,
    val facilityId: Long,
) : ChatRoom
