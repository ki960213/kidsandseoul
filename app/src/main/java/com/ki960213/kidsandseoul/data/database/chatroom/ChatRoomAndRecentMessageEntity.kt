package com.ki960213.kidsandseoul.data.database.chatroom

import androidx.room.Embedded
import androidx.room.Relation
import com.ki960213.domain.chat.model.FacilityChatRoom
import com.ki960213.domain.chat.model.PrivateChatRoom
import com.ki960213.kidsandseoul.data.database.message.MessageEntity

data class ChatRoomAndRecentMessageEntity(
    @Embedded val chatRoom: ChatRoomEntity,
    @Relation(
        parentColumn = "recentMessageId",
        entityColumn = "id"
    )
    val recentMessage: MessageEntity,
) {

    fun isPrivateChatRoom(): Boolean = chatRoom.isPrivateChatRoom()

    fun asPrivateChatRoom(): PrivateChatRoom {
        require(isPrivateChatRoom()) { "개인 채팅방이 아닙니다." }
        return chatRoom.toPrivateChatRoom(recentMessage.asMessage())
    }

    fun toFacilityChatRoom(memberIds: List<String>, facilityId: Long): FacilityChatRoom {
        require(!chatRoom.isPrivateChatRoom()) { "시설 채팅방이 아닙니다." }
        return FacilityChatRoom(
            id = chatRoom.id,
            name = chatRoom.name,
            unreadMessageCount = chatRoom.unreadMessageCount,
            recentMessage = recentMessage.asMessage(),
            isNotificationTurnOn = chatRoom.isNotificationTurnOn,
            memberIds = memberIds,
            facilityId = facilityId,
        )
    }
}
