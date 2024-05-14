package com.ki960213.domain.comment.model

import java.time.LocalDateTime

data class Comment(
    val id: String,
    val authorId: String,
    val postId: String,
    val parentCommentId: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val content: String,
    val likeCount: Int,
) {

    val isChildComment: Boolean = parentCommentId != null
    val isParentComment = !isChildComment

    fun isChildCommentOf(comment: Comment): Boolean = parentCommentId == comment.id
}
