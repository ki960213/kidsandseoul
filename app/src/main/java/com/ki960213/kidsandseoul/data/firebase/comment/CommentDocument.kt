package com.ki960213.kidsandseoul.data.firebase.comment

import androidx.annotation.Keep
import com.ki960213.domain.comment.model.Comment
import com.ki960213.kidsandseoul.data.firebase.Document
import com.ki960213.kidsandseoul.data.firebase.toLocalDateTime
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CommentDocument(
    val id: String = "",
    val authorId: String = "",
    val postId: String = "",
    val parentId: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val content: String = "",
    val likeCount: Int = 0,
) : Document {

    override fun isValid(): Boolean = id.isNotBlank()

    fun asComment() = Comment(
        id = id,
        authorId = authorId,
        postId = postId,
        parentCommentId = parentId.ifBlank { null },
        createdAt = createdAt.toLocalDateTime(),
        updatedAt = updatedAt.toLocalDateTime(),
        content = content,
        likeCount = likeCount,
    )
}
