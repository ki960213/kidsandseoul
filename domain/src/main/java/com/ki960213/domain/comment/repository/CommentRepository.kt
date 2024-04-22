package com.ki960213.domain.comment.repository

import com.ki960213.domain.comment.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    /**
     * 게시글의 댓글 목록 조회
     * @param[postId] 게시글 아이디
     * @return [Comment] 목록 [Flow]
     */
    fun getCommentsOfPost(postId: String): Flow<List<Comment>>

    /**
     * 유저가 작성한 댓글 목록 조회
     * @param[userId] 댓글 작성한 유저 아이디
     * @return [Comment] 목록 [Flow]
     */
    fun getCommentsOfUser(userId: String): Flow<List<Comment>>

    /**
     * @throws[NoSuchElementException] 댓글이 작성될 게시글이 없을 경우
     */
    suspend fun writeComment(authorId: String, postId: String, content: String)

    /**
     * 댓글이 없으면 아무 일도 하지 않음
     */
    suspend fun updateComment(commentId: String, content: String)

    suspend fun deleteComment(postId: String, commentId: String)
}
