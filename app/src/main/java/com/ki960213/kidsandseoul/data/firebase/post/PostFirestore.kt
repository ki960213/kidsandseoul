package com.ki960213.kidsandseoul.data.firebase.post

import com.ki960213.kidsandseoul.data.firebase.COLLECTION_COMMENTS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_POSTS
import com.ki960213.kidsandseoul.data.firebase.comment.CommentDocument
import com.ki960213.kidsandseoul.data.firebase.documentFlow
import com.ki960213.kidsandseoul.data.firebase.documentsFlow
import com.ki960213.kidsandseoul.data.firebase.runBatch
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    fun getPost(
        postId: String,
    ): Flow<PostDocument?> = db.collection(COLLECTION_POSTS)
        .document(postId)
        .documentFlow<PostDocument>()
        .flowOn(Dispatchers.IO)

    fun getRecentPosts(itemCount: Int): Flow<List<PostDocument>> =
        db.collection(COLLECTION_POSTS)
            .orderBy(PostDocument::createdAt.name, Direction.DESCENDING)
            .limit(itemCount)
            .documentsFlow<PostDocument>()
            .flowOn(Dispatchers.IO)

    fun getPostsOfUser(
        userId: String,
    ): Flow<List<PostDocument>> = db.collection(COLLECTION_POSTS)
        .where { PostDocument::authorId.name equalTo userId }
        .orderBy(PostDocument::createdAt.name, Direction.DESCENDING)
        .documentsFlow<PostDocument>()
        .flowOn(Dispatchers.IO)

    fun getLatestPostOfUser(
        userId: String,
    ): Flow<PostDocument?> = db.collection(COLLECTION_POSTS)
        .where { PostDocument::authorId.name equalTo userId }
        .orderBy(PostDocument::createdAt.name, Direction.DESCENDING)
        .limit(1)
        .documentsFlow<PostDocument>()
        .map { it.firstOrNull() }
        .flowOn(Dispatchers.IO)

    suspend fun createPost(
        authorId: String,
        title: String,
        content: String,
    ): String = withContext(Dispatchers.IO) {
        val postRef = db.collection(COLLECTION_POSTS).document
        val postDocument = PostDocument(
            id = postRef.id,
            authorId = authorId,
            title = title,
            content = content,
        )
        postRef.set(postDocument)
        postRef.id
    }

    suspend fun deletePost(postId: String) = withContext(Dispatchers.IO) {
        val postRef = db.collection(COLLECTION_POSTS).document(postId)
        val commentRefs = db.collection(COLLECTION_COMMENTS)
            .where { CommentDocument::postId.name equalTo postId }
            .get()
            .documents
            .map { it.reference }
        db.runBatch {
            delete(postRef)
            commentRefs.forEach { delete(it) }
        }
    }
}
