package com.ki960213.kidsandseoul.presentation.ui.chatroom.messages

import java.time.LocalDateTime

sealed interface MessageUiState {

    val createdAt: LocalDateTime
}

data class MyTextMessageUiState(
    override val createdAt: LocalDateTime,
    val text: String,
) : MessageUiState

data class OtherTextMessageUiState(
    override val createdAt: LocalDateTime,
    val authorId: String,
    val authorName: String,
    val authorProfileImageUrl: String,
    val isShowProfile: Boolean,
    val text: String,
) : MessageUiState

data class MyImageMessageUiState(
    override val createdAt: LocalDateTime,
    val imageUrl: String,
) : MessageUiState

data class OtherImageMessageUiState(
    override val createdAt: LocalDateTime,
    val authorId: String,
    val authorName: String,
    val authorProfileImageUrl: String,
    val isShowProfile: Boolean,
    val imageUrl: String,
) : MessageUiState

data class InOutUiState(
    override val createdAt: LocalDateTime,
    val authorName: String,
    val isIn: Boolean,
) : MessageUiState

data class DateUiState(
    override val createdAt: LocalDateTime,
) : MessageUiState
