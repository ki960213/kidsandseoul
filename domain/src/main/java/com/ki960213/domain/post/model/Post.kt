package com.ki960213.domain.post.model

import java.time.LocalDateTime

data class Post(
    val id: String,
    val authorId: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val title: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
) {

    val isUpdated: Boolean = createdAt != updatedAt
}
