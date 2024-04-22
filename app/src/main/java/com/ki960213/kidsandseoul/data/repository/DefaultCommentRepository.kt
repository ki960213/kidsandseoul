package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.comment.model.Comment
import com.ki960213.domain.comment.repository.CommentRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.comment.CommentDocument
import com.ki960213.kidsandseoul.data.firebase.comment.CommentFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultCommentRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val commentFirestore: CommentFirestore,
) : CommentRepository {

    override fun getCommentsOfPost(
        postId: String,
    ): Flow<List<Comment>> = commentFirestore.getCommentsOfPost(postId)
        .map { it.asComments() }

    override fun getCommentsOfUser(
        userId: String,
    ): Flow<List<Comment>> = commentFirestore.getCommentsOfUser(userId)
        .map { it.asComments() }

    private fun List<CommentDocument>.asComments(): List<Comment> = map { it.asComment() }

    override suspend fun writeComment(
        authorId: String,
        postId: String,
        content: String,
    ) = externalScope.launch {
        commentFirestore.writeComment(
            authorId = authorId,
            postId = postId,
            content = content,
        )
    }.join()

    override suspend fun updateComment(commentId: String, content: String) {
        commentFirestore.updateComment(
            commentId = commentId,
            content = content,
        )
    }

    override suspend fun deleteComment(postId: String, commentId: String) {
        commentFirestore.deleteComment(postId, commentId)
    }
}
