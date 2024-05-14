package com.ki960213.kidsandseoul.data.firebase.post

import androidx.annotation.Keep
import com.ki960213.domain.post.model.Post
import com.ki960213.kidsandseoul.data.firebase.Document
import com.ki960213.kidsandseoul.data.firebase.toLocalDateTime
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PostDocument(
    val id: String = "",
    val authorId: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
) : Document {

    override fun isValid(): Boolean = id.isNotBlank()

    fun asPost() = Post(
        id = id,
        authorId = authorId,
        createdAt = createdAt.toLocalDateTime(),
        updatedAt = updatedAt.toLocalDateTime(),
        content = content,
        likeCount = likeCount,
        commentCount = commentCount,
        title = title
    )
}
