package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.review.model.Review
import com.ki960213.domain.review.model.StarPoint
import com.ki960213.domain.review.repository.ReviewRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.ImageStorage
import com.ki960213.kidsandseoul.data.firebase.review.ReviewDocument
import com.ki960213.kidsandseoul.data.firebase.review.ReviewFirestore
import com.ki960213.kidsandseoul.data.network.facility.FacilityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultReviewRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val firestore: ReviewFirestore,
    private val imageStorage: ImageStorage,
    private val facilityApi: FacilityApi,
) : ReviewRepository {

    override suspend fun getRecentReviews(offset: Int, itemCount: Int): List<Review> = firestore
        .fetchRecentReviews(offset, itemCount)
        .map { it.asReview() }

    override fun getRecentReviews(): Flow<List<Review>> = firestore
        .getRecentReviews(RECENT_REVIEW_COUNT)
        .map(::reviewDocumentsToReviews)

    override fun getReviews(facilityId: Long): Flow<List<Review>> = firestore.getReviews(facilityId)
        .map(::reviewDocumentsToReviews)

    override fun getReviewsOfUser(userId: String): Flow<List<Review>> =
        firestore.getReviewsOfUser(userId)
            .map(::reviewDocumentsToReviews)

    private fun reviewDocumentsToReviews(documents: List<ReviewDocument>): List<Review> =
        documents.map(ReviewDocument::asReview)

    override fun getLatestReviewOfUser(userId: String): Flow<Review?> =
        firestore.getLatestReviewOfUser(userId)
            .map { it?.asReview() }

    override fun isExistReviewOfUser(facilityId: Long, userId: String): Flow<Boolean> =
        firestore.isExistReviewOfUser(facilityId, userId)

    override suspend fun createReview(
        authorId: String,
        facilityId: Long,
        starPoint: StarPoint,
        content: String,
        imageFiles: List<File>,
    ) = externalScope.launch {
        val imageNames = List(imageFiles.size) {
            Review.createImageName(
                facilityId = facilityId,
                authorId = authorId,
                imageNumber = it + 1
            )
        }
        facilityApi.addReviewOf(facilityId, starPoint.value)
        val imageUrls = imageStorage.uploadImages(imageNames.zip(imageFiles))
        firestore.createReview(
            authorId = authorId,
            facilityId = facilityId,
            starPoint = starPoint.value,
            content = content,
            imageUrls = imageUrls
        )
    }.join()

    override suspend fun deleteReview(reviewId: String) = externalScope.launch {
        val review = firestore.fetchReview(reviewId)
            ?.asReview()
            ?: return@launch
        facilityApi.deleteReviewOf(review.facilityId, review.starPoint.value)
        launch { imageStorage.deleteImages(review.imageNames) }
        launch { firestore.deleteReview(reviewId) }
    }.join()

    companion object {

        private const val RECENT_REVIEW_COUNT = 100
    }
}
