package com.ki960213.kidsandseoul.data.database.message

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ki960213.domain.chat.model.ImageMessage
import com.ki960213.domain.chat.model.InOutMessage
import com.ki960213.domain.chat.model.TextMessage
import com.ki960213.kidsandseoul.data.database.chatroom.ChatRoomEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

@Entity(
    tableName = "messages",
    foreignKeys = [ForeignKey(
        entity = ChatRoomEntity::class,
        parentColumns = ["id"],
        childColumns = ["chatRoomId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val createdAt: LocalDateTime,
    val authorId: String,
    val chatRoomId: String,
    val content: String, // Json 형식 { "type": "Text|Image|InOut", ("text": "Asdf"|"imageUrl": "asdf"|"isIn": true) }
) {

    fun asMessage() = when (val content = Json.decodeFromString<Content>(content)) {
        is Text -> TextMessage(
            id = id,
            chatRoomId = chatRoomId,
            authorId = authorId,
            createdAt = createdAt,
            text = content.text,
        )

        is Image -> ImageMessage(
            id = id,
            chatRoomId = chatRoomId,
            authorId = authorId,
            createdAt = createdAt,
            imageUrl = content.imageUrl,
        )

        is InOut -> InOutMessage(
            id = id,
            chatRoomId = chatRoomId,
            authorId = authorId,
            createdAt = createdAt,
            isIn = content.isIn,
        )
    }
}

@Serializable
sealed class Content

@Serializable
data class Text(val text: String) : Content()

@Serializable
data class Image(val imageUrl: String) : Content()

@Serializable
data class InOut(val isIn: Boolean) : Content()
