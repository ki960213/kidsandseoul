package com.ki960213.domain.review.repository

import com.ki960213.domain.review.model.Review
import com.ki960213.domain.review.model.StarPoint
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    /**
     * offset부터 itemCount개만큼 리뷰 목록 조회
     * @param[offset] 조회를 시작할 오프셋
     * @param[itemCount] 조회할 [Review] 개수
     * @return [Review] 목록
     */
    suspend fun getRecentReviews(offset: Int, itemCount: Int): List<Review>

    /**
     * 특정 시설의 리뷰 목록 조회
     * @param[facilityId] 시설 아이디
     * @return [Review] 목록 [Flow]
     */
    fun getReviews(facilityId: Long): Flow<List<Review>>

    /**
     * 유저가 작성한 리뷰 목록 조회
     * @param[userId] 유저 아이디
     * @return [Review] 목록 [Flow]
     */
    fun getReviewsOfUser(userId: String): Flow<List<Review>>

    suspend fun createReview(
        authorId: String,
        facilityId: Long,
        starPoint: StarPoint,
        content: String,
        imageUrls: List<String>,
    ): String

    suspend fun deleteReview(reviewId: String)
}
