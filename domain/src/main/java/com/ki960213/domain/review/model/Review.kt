package com.ki960213.domain.review.model

import java.time.LocalDateTime

data class Review(
    val id: String,
    val authorId: String,
    val facilityId: Long,
    val starPoint: StarPoint,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val content: String,
    val imageUrls: List<String>,
    val likeCount: Int,
)
