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
) {

    val imageNames: List<String> = List(imageUrls.size) { index ->
        createImageName(
            facilityId = facilityId,
            authorId = authorId,
            imageNumber = index + 1
        )
    }

    companion object {
        fun createImageName(facilityId: Long, authorId: String, imageNumber: Int): String {
            return "$facilityId-$authorId-$imageNumber"
        }
    }
}
