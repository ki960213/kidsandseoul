package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.review.model.Review
import com.ki960213.domain.review.model.StarPoint
import com.ki960213.domain.review.repository.ReviewRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.review.ReviewFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultReviewRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val firestore: ReviewFirestore,
) : ReviewRepository {

    override suspend fun getRecentReviews(offset: Int, itemCount: Int): List<Review> = firestore
        .fetchRecentReviews(offset, itemCount)
        .map { it.asReview() }
        .sortedBy { it.createdAt }

    override fun getReviews(facilityId: Long): Flow<List<Review>> = firestore.getReviews(facilityId)
        .map { it.map { it.asReview() }.sortedBy { it.createdAt } }

    override fun getReviewsOfUser(userId: String): Flow<List<Review>> =
        firestore.getReviewsOfUser(userId)
            .map { it.map { it.asReview() }.sortedBy { it.createdAt } }

    override suspend fun createReview(
        authorId: String,
        facilityId: Long,
        starPoint: StarPoint,
        content: String,
        imageUrls: List<String>
    ): String = firestore.createReview(
        authorId = authorId,
        facilityId = facilityId,
        starPoint = starPoint.value,
        content = content,
        imageUrls = imageUrls,
    )

    override suspend fun deleteReview(reviewId: String) = externalScope.launch {
        firestore.deleteReview(reviewId)
    }.join()

    companion object {

        private const val TEMP_REVIEW_COUNT = 100L
    }
}
