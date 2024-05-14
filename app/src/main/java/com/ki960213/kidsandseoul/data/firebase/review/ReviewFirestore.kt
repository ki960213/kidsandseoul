package com.ki960213.kidsandseoul.data.firebase.review

import com.ki960213.kidsandseoul.data.firebase.COLLECTION_REVIEWS
import com.ki960213.kidsandseoul.data.firebase.documentsFlow
import com.ki960213.kidsandseoul.data.firebase.fetchDocument
import com.ki960213.kidsandseoul.data.firebase.fetchDocuments
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.startAt
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReviewFirestore @Inject constructor(private val db: FirebaseFirestore) {

    suspend fun fetchRecentReviews(
        offset: Int,
        itemCount: Int,
    ): List<ReviewDocument> = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_REVIEWS)
            .orderBy(ReviewDocument::createdAt.name, Direction.DESCENDING)
            .startAt(offset)
            .limit(itemCount)
            .fetchDocuments()
    }

    fun getRecentReviews(size: Int): Flow<List<ReviewDocument>> =
        db.collection(COLLECTION_REVIEWS)
            .orderBy(ReviewDocument::createdAt.name, Direction.DESCENDING)
            .limit(size)
            .documentsFlow<ReviewDocument>()
            .flowOn(Dispatchers.IO)

    suspend fun fetchReview(reviewId: String): ReviewDocument? =
        withContext(Dispatchers.IO) {
            db.collection(COLLECTION_REVIEWS)
                .document(reviewId)
                .fetchDocument()
        }

    fun getReviews(
        facilityId: Long,
    ): Flow<List<ReviewDocument>> = db.collection(COLLECTION_REVIEWS)
        .where { ReviewDocument::facilityId.name equalTo facilityId }
        .orderBy(ReviewDocument::createdAt.name, Direction.DESCENDING)
        .documentsFlow<ReviewDocument>()
        .flowOn(Dispatchers.IO)

    fun getReviewsOfUser(
        userId: String,
    ): Flow<List<ReviewDocument>> = db.collection(COLLECTION_REVIEWS)
        .where { ReviewDocument::authorId.name equalTo userId }
        .orderBy(ReviewDocument::createdAt.name, Direction.DESCENDING)
        .documentsFlow<ReviewDocument>()
        .flowOn(Dispatchers.IO)

    fun getLatestReviewOfUser(
        userId: String,
    ): Flow<ReviewDocument?> = db.collection(COLLECTION_REVIEWS)
        .where { ReviewDocument::authorId.name equalTo userId }
        .orderBy(ReviewDocument::createdAt.name, Direction.DESCENDING)
        .limit(1)
        .documentsFlow<ReviewDocument>()
        .map { it.firstOrNull() }
        .flowOn(Dispatchers.IO)

    fun isExistReviewOfUser(
        facilityId: Long,
        userId: String,
    ): Flow<Boolean> = db.collection(COLLECTION_REVIEWS)
        .where {
            all(
                ReviewDocument::facilityId.name equalTo facilityId,
                ReviewDocument::authorId.name equalTo userId
            )
        }.snapshots
        .map { it.documents.isNotEmpty() }

    suspend fun createReview(
        authorId: String,
        facilityId: Long,
        starPoint: Int,
        content: String,
        imageUrls: List<String>,
    ) = withContext(Dispatchers.IO) {
        val reviewRef = db.collection(COLLECTION_REVIEWS).document
        val reviewDocument = ReviewDocument(
            id = reviewRef.id,
            authorId = authorId,
            facilityId = facilityId,
            content = content,
            imageUrls = imageUrls,
            starPoint = starPoint,
        )
        db.collection(COLLECTION_REVIEWS)
            .document(reviewRef.id)
            .set(reviewDocument)
    }

    suspend fun deleteReview(reviewId: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_REVIEWS)
            .document(reviewId)
            .delete()
    }
}
