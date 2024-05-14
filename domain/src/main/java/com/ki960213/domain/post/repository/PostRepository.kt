package com.ki960213.domain.post.repository

import com.ki960213.domain.post.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    /**
     * @throws[NoSuchElementException] 해당 아이디의 게시글이 존재하지 않을 경우
     */
    fun getPost(postId: String): Flow<Post?>

    /**
     * 최신 100개 게시글 목록 조회
     * @return [Post] 목록
     */
    fun getRecentPosts(): Flow<List<Post>>

    /**
     * 유저가 작성한 게시글 목록 조회
     * @param[userId] 유저 아이디
     * @return [Review] 목록 [Flow]
     */
    fun getPostsOfUser(userId: String): Flow<List<Post>>

    fun getLatestPostOfUser(userId: String): Flow<Post?>

    /**
     * @return 게시글 아이디
     */
    suspend fun createPost(
        authorId: String,
        title: String,
        content: String,
    ): String

    /**
     * 게시글 삭제
     * @param[postId] 삭제할 게시글 아이디
     */
    suspend fun deletePost(postId: String)

    /**
     * 게시글 좋아요 하기
     *
     * 이미 좋아요 되어 있다면 아무 일도 안함
     * @param[userId] 좋아요 하려는 유저 아이디
     * @param[postId] 좋아요 할 게시글 아이디
     */
    suspend fun like(userId: String, postId: String)

    /**
     * 게시글 좋아요 취소
     *
     * 이미 좋아요 해제 되어 있다면 아무 일도 안함
     * @param[userId] 좋아요 취소 하려는 유저 아이디
     * @param[postId] 좋아요 취소 할 게시글 아이디
     */
    suspend fun unlike(userId: String, postId: String)
}
