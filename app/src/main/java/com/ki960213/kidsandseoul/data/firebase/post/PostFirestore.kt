package com.ki960213.kidsandseoul.data.firebase.post

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_COMMENTS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_POSTS
import com.ki960213.kidsandseoul.data.firebase.comment.CommentDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    fun getPost(
        postId: String,
    ): Flow<PostDocument?> = db.collection(COLLECTION_POSTS)
        .document(postId)
        .snapshots()
        .map { it.toObject<PostDocument>() }

    fun fetchPosts(itemCount: Long): Flow<List<PostDocument>> =
        db.collection(COLLECTION_POSTS)
            .orderBy(PostDocument.FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .limit(itemCount)
            .snapshots()
            .map { it.toObjects<PostDocument>() }
            .flowOn(Dispatchers.IO)

    fun getPostsOfUser(
        userId: String,
    ): Flow<List<PostDocument>> = db.collection(COLLECTION_POSTS)
        .whereEqualTo(PostDocument.FIELD_AUTHOR_ID, userId)
        .snapshots()
        .map { it.toObjects<PostDocument>() }
        .flowOn(Dispatchers.IO)

    suspend fun createPost(
        authorId: String,
        title: String,
        content: String,
    ): String = withContext(Dispatchers.IO) {
        val documentReference = db.collection(COLLECTION_POSTS).document()
        val postDocument = PostDocument(
            id = documentReference.id,
            authorId = authorId,
            title = title,
            content = content,
        )
        documentReference.set(postDocument)
        documentReference.id
    }

    suspend fun deletePost(postId: String) = withContext(Dispatchers.IO) {
        val postRef = db.collection(COLLECTION_POSTS).document(postId)
        val commentRefs = db.collection(COLLECTION_COMMENTS)
            .whereEqualTo(CommentDocument.FIELD_POST_ID, postId)
            .get()
            .await()
            .map { it.reference }
        db.runBatch { batch ->
            batch.delete(postRef)
            commentRefs.forEach { batch.delete(it) }
        }
    }
}
