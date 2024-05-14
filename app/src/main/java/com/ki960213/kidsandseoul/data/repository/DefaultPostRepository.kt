package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.post.model.Post
import com.ki960213.domain.post.repository.PostRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.post.PostDocument
import com.ki960213.kidsandseoul.data.firebase.post.PostFirestore
import com.ki960213.kidsandseoul.data.firebase.user.UserFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultPostRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val postFirestore: PostFirestore,
    private val userFirestore: UserFirestore,
) : PostRepository {

    override fun getPost(
        postId: String,
    ): Flow<Post?> = postFirestore.getPost(postId)
        .map { it?.asPost() }

    override fun getRecentPosts(): Flow<List<Post>> = postFirestore
        .getRecentPosts(RECENT_POST_COUNT)
        .map { it.toPosts() }

    override fun getPostsOfUser(
        userId: String,
    ): Flow<List<Post>> = postFirestore
        .getPostsOfUser(userId)
        .map { it.toPosts() }

    override fun getLatestPostOfUser(userId: String): Flow<Post?> = postFirestore
        .getLatestPostOfUser(userId)
        .map { it?.asPost() }

    private fun List<PostDocument>.toPosts(): List<Post> = map(PostDocument::asPost)

    override suspend fun createPost(
        authorId: String,
        title: String,
        content: String,
    ): String = externalScope.async {
        postFirestore.createPost(authorId, title, content)
    }.await()

    override suspend fun deletePost(postId: String) = externalScope.launch {
        postFirestore.deletePost(postId)
    }.join()

    override suspend fun like(userId: String, postId: String) = externalScope.launch {
        userFirestore.addLikePostId(userId, postId)
    }.join()

    override suspend fun unlike(userId: String, postId: String) = externalScope.launch {
        userFirestore.deleteLikePostId(userId, postId)
    }.join()

    companion object {

        private const val RECENT_POST_COUNT = 100
    }
}
