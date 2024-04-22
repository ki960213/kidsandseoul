package com.ki960213.domain.chat.model

import java.time.LocalDateTime

sealed interface Message {

    val id: Long
    val chatRoomId: String
    val authorId: String
    val createdAt: LocalDateTime
}

data class TextMessage(
    override val id: Long,
    override val chatRoomId: String,
    override val authorId: String,
    override val createdAt: LocalDateTime,
    val text: String,
) : Message

data class ImageMessage(
    override val id: Long,
    override val chatRoomId: String,
    override val authorId: String,
    override val createdAt: LocalDateTime,
    val imageUrl: String,
) : Message

data class InOutMessage(
    override val id: Long,
    override val chatRoomId: String,
    override val authorId: String,
    override val createdAt: LocalDateTime,
    val isIn: Boolean,
) : Message
