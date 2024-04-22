package com.ki960213.kidsandseoul.data.network.fcm.model

import com.ki960213.domain.chat.model.ImageMessage
import com.ki960213.domain.chat.model.InOutMessage
import com.ki960213.domain.chat.model.Message
import com.ki960213.domain.chat.model.TextMessage
import com.ki960213.kidsandseoul.data.network.serializer.DateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
sealed interface NetworkMessage {

    val id: Long
    val chatRoomId: String
    val authorId: String
    val createdAt: LocalDateTime

    companion object {

        fun from(message: Message): NetworkMessage = when (message) {
            is TextMessage -> NetworkTextMessage(message)
            is ImageMessage -> NetworkImageMessage(message)
            is InOutMessage -> NetworkInOutMessage(message)
        }
    }
}

@Serializable
data class NetworkTextMessage(
    override val id: Long,
    override val chatRoomId: String,
    override val authorId: String,
    @Serializable(with = DateTimeSerializer::class)
    override val createdAt: LocalDateTime,
    val text: String,
) : NetworkMessage {

    constructor(textMessage: TextMessage) : this(
        id = textMessage.id,
        chatRoomId = textMessage.chatRoomId,
        authorId = textMessage.authorId,
        createdAt = textMessage.createdAt,
        text = textMessage.text,
    )
}

@Serializable
data class NetworkImageMessage(
    override val id: Long,
    override val chatRoomId: String,
    override val authorId: String,
    @Serializable(with = DateTimeSerializer::class)
    override val createdAt: LocalDateTime,
    val imageUrl: String,
) : NetworkMessage {

    constructor(imageMessage: ImageMessage) : this(
        id = imageMessage.id,
        chatRoomId = imageMessage.chatRoomId,
        authorId = imageMessage.authorId,
        createdAt = imageMessage.createdAt,
        imageUrl = imageMessage.imageUrl,
    )
}

@Serializable
data class NetworkInOutMessage(
    override val id: Long,
    override val chatRoomId: String,
    override val authorId: String,
    @Serializable(with = DateTimeSerializer::class)
    override val createdAt: LocalDateTime,
    val isIn: Boolean,
) : NetworkMessage {

    constructor(inOutMessage: InOutMessage) : this(
        id = inOutMessage.id,
        chatRoomId = inOutMessage.chatRoomId,
        authorId = inOutMessage.authorId,
        createdAt = inOutMessage.createdAt,
        isIn = inOutMessage.isIn,
    )
}
