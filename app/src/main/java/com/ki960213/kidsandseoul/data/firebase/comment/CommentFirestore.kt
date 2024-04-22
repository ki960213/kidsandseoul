package com.ki960213.kidsandseoul.data.firebase.comment

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_COMMENTS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_POSTS
import com.ki960213.kidsandseoul.data.firebase.post.PostDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommentFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    fun getCommentsOfPost(
        postId: String,
    ): Flow<List<CommentDocument>> = db.collection(COLLECTION_COMMENTS)
        .whereEqualTo(CommentDocument.FIELD_POST_ID, postId)
        .snapshots()
        .map { it.toObjects<CommentDocument>() }
        .flowOn(Dispatchers.IO)

    fun getCommentsOfUser(
        userId: String,
    ): Flow<List<CommentDocument>> = db.collection(COLLECTION_COMMENTS)
        .whereEqualTo(CommentDocument.FIELD_AUTHOR_ID, userId)
        .snapshots()
        .map { it.toObjects<CommentDocument>() }
        .flowOn(Dispatchers.IO)

    suspend fun writeComment(
        authorId: String,
        postId: String,
        content: String,
    ) = withContext(Dispatchers.IO) {
        val commentRef = db.collection(COLLECTION_COMMENTS).document()
        val postRef = db.collection(COLLECTION_POSTS).document(postId)
        db.runTransaction { transaction ->
            val postDocument = transaction.get(postRef).toObject<PostDocument>()
            val isPostExist = postDocument != null
            if (!isPostExist) return@runTransaction //삭제된 게시글이므로
            val data = CommentDocument(
                id = commentRef.id,
                authorId = authorId,
                postId = postRef.id,
                content = content,
            )
            transaction.set(commentRef, data)
            transaction.update(
                postRef,
                PostDocument.FIELD_COMMENT_COUNT,
                postDocument!!.commentCount + 1,
            )
        }
    }

    suspend fun updateComment(commentId: String, content: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_COMMENTS)
            .document(commentId)
            .update(
                CommentDocument.FIELD_CONTENT, content,
                CommentDocument.FIELD_UPDATED_AT, Timestamp.now()
            )
    }

    suspend fun deleteComment(postId: String, commentId: String) = withContext(Dispatchers.IO) {
        val commentRef = db.collection(COLLECTION_COMMENTS).document(commentId)
        val postRef = db.collection(COLLECTION_POSTS).document(postId)
        db.runTransaction { transaction ->
            val postDocument = transaction.get(postRef).toObject<PostDocument>()
            val isPostExist = postDocument != null
            if (!isPostExist) return@runTransaction //삭제된 게시글이므로
            transaction.delete(commentRef)
            transaction.update(
                postRef,
                PostDocument.FIELD_COMMENT_COUNT,
                postDocument!!.commentCount - 1,
            )
        }
    }
}
