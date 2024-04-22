package com.ki960213.kidsandseoul.data.firebase.review

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.ki960213.domain.review.model.Review
import com.ki960213.domain.review.model.StarPoint
import com.ki960213.kidsandseoul.data.firebase.toLocalDateTime

@Keep
data class ReviewDocument(
    @JvmField @PropertyName(FIELD_ID) val id: String = "",
    @JvmField @PropertyName(FIELD_AUTHOR_ID) val authorId: String = "",
    @JvmField @PropertyName(FIELD_FACILITY_ID) val facilityId: Long = -1,
    @JvmField @PropertyName(FIELD_STAR_POINT) val starPoint: Int = -1,
    @JvmField @PropertyName(FIELD_CREATED_AT) val createdAt: Timestamp = Timestamp.now(),
    @JvmField @PropertyName(FIELD_UPDATED_AT) val updatedAt: Timestamp = Timestamp.now(),
    @JvmField @PropertyName(FIELD_CONTENT) val content: String = "",
    @JvmField @PropertyName(FIELD_IMAGE_URLS) val imageUrls: List<String> = emptyList(),
    @JvmField @PropertyName(FIELD_LIKE_COUNT) val likeCount: Int = -1,
) {

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

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_AUTHOR_ID = "authorId"
        const val FIELD_FACILITY_ID = "facilityId"
        const val FIELD_STAR_POINT = "starPoint"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_CONTENT = "content"
        const val FIELD_IMAGE_URLS = "imageUrls"
        const val FIELD_LIKE_COUNT = "likeCount"
    }
}
