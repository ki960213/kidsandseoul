package com.ki960213.kidsandseoul.data.database.chatroom

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ki960213.domain.chat.model.Message
import com.ki960213.domain.chat.model.PrivateChatRoom

@Entity("chat_rooms")
data class ChatRoomEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val unreadMessageCount: Int = 0,
    val recentMessageId: Long? = null,
    val isNotificationTurnOn: Boolean = true,
    val interlocutorId: String? = null,
) {

    fun isPrivateChatRoom(): Boolean = interlocutorId != null

    fun toPrivateChatRoom(recentMessage: Message): PrivateChatRoom {
        require(isPrivateChatRoom()) { "개인 채팅방이 아닙니다." }
        return PrivateChatRoom(
            id = id,
            name = name,
            unreadMessageCount = unreadMessageCount,
            recentMessage = recentMessage,
            isNotificationTurnOn = isNotificationTurnOn,
            interlocutorId = interlocutorId!!,
        )
    }
}
