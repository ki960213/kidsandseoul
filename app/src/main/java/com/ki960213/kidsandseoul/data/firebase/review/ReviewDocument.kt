package com.ki960213.kidsandseoul.data.firebase.review

import androidx.annotation.Keep
import com.ki960213.domain.review.model.Review
import com.ki960213.domain.review.model.StarPoint
import com.ki960213.kidsandseoul.data.firebase.Document
import com.ki960213.kidsandseoul.data.firebase.toLocalDateTime
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ReviewDocument(
    val id: String = "",
    val authorId: String = "",
    val facilityId: Long = -1,
    val starPoint: Int = -1,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val content: String = "",
    val imageUrls: List<String> = emptyList(),
    val likeCount: Int = 0,
) : Document {

    override fun isValid(): Boolean = id.isNotBlank()

    fun asReview() = Review(
        id = id,
        authorId = authorId,
        facilityId = facilityId,
        starPoint = StarPoint(starPoint),
        createdAt = createdAt.toLocalDateTime(),
        updatedAt = updatedAt.toLocalDateTime(),
        content = content,
        imageUrls = imageUrls,
        likeCount = likeCount,
    )
}
