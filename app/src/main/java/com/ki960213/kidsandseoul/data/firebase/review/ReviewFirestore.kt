package com.ki960213.kidsandseoul.data.firebase.review

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_FACILITIES
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_REVIEWS
import com.ki960213.kidsandseoul.data.firebase.facility.FacilityDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewFirestore @Inject constructor(private val db: FirebaseFirestore) {

    suspend fun fetchRecentReviews(
        offset: Int,
        itemCount: Int,
    ): List<ReviewDocument> = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_REVIEWS)
            .orderBy(ReviewDocument.FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .startAt(offset)
            .limit(itemCount.toLong())
            .get()
            .await()
            .toObjects()
    }

    fun getReviews(
        facilityId: Long,
    ): Flow<List<ReviewDocument>> = db.collection(COLLECTION_REVIEWS)
        .whereEqualTo(ReviewDocument.FIELD_FACILITY_ID, facilityId)
        .snapshots()
        .map { it.toObjects<ReviewDocument>() }
        .flowOn(Dispatchers.IO)

    fun getReviewsOfUser(
        userId: String,
    ): Flow<List<ReviewDocument>> = db.collection(COLLECTION_REVIEWS)
        .whereEqualTo(ReviewDocument.FIELD_AUTHOR_ID, userId)
        .snapshots()
        .map { it.toObjects<ReviewDocument>() }
        .flowOn(Dispatchers.IO)

    suspend fun createReview(
        authorId: String,
        facilityId: Long,
        starPoint: Int,
        content: String,
        imageUrls: List<String>,
    ) = withContext(Dispatchers.IO) {
        val reviewRef = db.collection(COLLECTION_REVIEWS).document()
        val facilityRef = db.collection(COLLECTION_FACILITIES)
            .whereEqualTo(FacilityDocument.FIELD_ID, facilityId)
            .get()
            .await()
            .firstOrNull()
            ?.reference
            ?: db.collection(COLLECTION_FACILITIES).document()
        val reviewDocument = ReviewDocument(
            id = reviewRef.id,
            authorId = authorId,
            facilityId = facilityId,
            content = content,
            imageUrls = imageUrls,
            starPoint = starPoint,
        )
        db.runTransaction { transaction ->
            val facilityDocument = transaction.get(facilityRef)
                .toObject<FacilityDocument>()
                ?: FacilityDocument(id = facilityId)
            val newFacilityDocument = facilityDocument.copy(
                reviewCount = facilityDocument.reviewCount + 1,
                starPointAvg = (facilityDocument.starPointAvg * facilityDocument.starPointCount + starPoint) / (facilityDocument.starPointCount + 1),
                starPointCount = facilityDocument.starPointCount + 1
            )
            transaction.set(facilityRef, newFacilityDocument)
            transaction.set(reviewRef, reviewDocument)
        }
        reviewRef.id
    }

    suspend fun deleteReview(reviewId: String) = withContext(Dispatchers.IO) {
        val reviewRef = db.collection(COLLECTION_REVIEWS).document(reviewId)
        val reviewDocument = reviewRef.get()
            .await()
            .toObject<ReviewDocument>()
            ?: return@withContext
        val facilityRef = db.collection(COLLECTION_FACILITIES)
            .whereEqualTo(FacilityDocument.FIELD_ID, reviewDocument.facilityId)
            .get()
            .await()
            .firstOrNull()
            ?.reference
        db.runTransaction { transaction ->
            if (facilityRef != null) {
                val facilityDocument = transaction.get(facilityRef)
                    .toObject<FacilityDocument>()
                val newStarPointCount = (facilityDocument?.starPointCount ?: 1) - 1
                val newFacilityDocument = facilityDocument?.copy(
                    reviewCount = facilityDocument.reviewCount - 1,
                    starPointAvg = if (newStarPointCount == 0) {
                        0.0
                    } else {
                        (facilityDocument.starPointAvg * facilityDocument.starPointCount - reviewDocument.starPoint) / newStarPointCount
                    },
                    starPointCount = facilityDocument.starPointCount - 1,
                )
                if (newFacilityDocument != null) transaction.set(facilityRef, newFacilityDocument)
            }
            transaction.delete(reviewRef)
        }
    }
}
