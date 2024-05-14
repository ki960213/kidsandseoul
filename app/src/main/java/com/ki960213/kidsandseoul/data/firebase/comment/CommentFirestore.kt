package com.ki960213.kidsandseoul.data.firebase.comment

import com.ki960213.kidsandseoul.data.firebase.COLLECTION_COMMENTS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_POSTS
import com.ki960213.kidsandseoul.data.firebase.documentsFlow
import com.ki960213.kidsandseoul.data.firebase.fetchDocument
import com.ki960213.kidsandseoul.data.firebase.post.PostDocument
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommentFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    fun getCommentsOfPost(
        postId: String,
    ): Flow<List<CommentDocument>> = db.collection(COLLECTION_COMMENTS)
        .where { CommentDocument::postId.name equalTo postId }
        .documentsFlow()

    fun getCommentsOfUser(
        userId: String,
    ): Flow<List<CommentDocument>> = db.collection(COLLECTION_COMMENTS)
        .where { CommentDocument::authorId.name equalTo userId }
        .documentsFlow()

    suspend fun writeComment(
        authorId: String,
        postId: String,
        content: String,
    ) = withContext(Dispatchers.IO) {
        val commentRef = db.collection(COLLECTION_COMMENTS).document
        val postRef = db.collection(COLLECTION_POSTS).document(postId)
        db.runTransaction {
            val postDocument = get(postRef).fetchDocument<PostDocument>() ?: return@runTransaction
            val commentDocument = CommentDocument(
                id = commentRef.id,
                authorId = authorId,
                postId = postRef.id,
                content = content,
            )
            set(commentRef, commentDocument)
            update(
                postRef,
                PostDocument::commentCount.name to postDocument.commentCount + 1,
            )
        }
    }

    suspend fun updateComment(
        commentId: String,
        content: String,
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_COMMENTS)
            .document(commentId)
            .update(
                CommentDocument::content.name to content,
                CommentDocument::updatedAt.name to Timestamp.now()
            )
    }

    suspend fun deleteComment(
        postId: String,
        commentId: String,
    ) = withContext(Dispatchers.IO) {
        val commentRef = db.collection(COLLECTION_COMMENTS).document(commentId)
        val postRef = db.collection(COLLECTION_POSTS).document(postId)
        db.runTransaction {
            val postDocument = get(postRef).fetchDocument<PostDocument>() ?: return@runTransaction
            delete(commentRef)
            update(
                postRef,
                PostDocument::commentCount.name to postDocument.commentCount - 1,
            )
        }
    }
}
