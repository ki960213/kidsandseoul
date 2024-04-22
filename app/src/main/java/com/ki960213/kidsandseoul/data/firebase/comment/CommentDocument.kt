package com.ki960213.kidsandseoul.data.firebase.comment

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.ki960213.domain.comment.model.Comment
import com.ki960213.kidsandseoul.data.firebase.toLocalDateTime

@Keep
data class CommentDocument(
    @JvmField @PropertyName(FIELD_ID) val id: String = "",
    @JvmField @PropertyName(FIELD_AUTHOR_ID) val authorId: String = "",
    @JvmField @PropertyName(FIELD_POST_ID) val postId: String = "",
    @JvmField @PropertyName(FIELD_PARENT_ID) val parentId: String = "",
    @JvmField @PropertyName(FIELD_CREATED_AT) val createdAt: Timestamp = Timestamp.now(),
    @JvmField @PropertyName(FIELD_UPDATED_AT) val updatedAt: Timestamp = Timestamp.now(),
    @JvmField @PropertyName(FIELD_CONTENT) val content: String = "",
    @JvmField @PropertyName(FIELD_LIKE_COUNT) val likeCount: Int = 0,
) {

    fun asComment() = Comment(
        id = id,
        authorId = authorId,
        postId = postId,
        parentCommentId = parentId.let { it.ifBlank { null } },
        createdAt = createdAt.toLocalDateTime(),
        updatedAt = updatedAt.toLocalDateTime(),
        content = content,
        likeCount = likeCount,
    )

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_AUTHOR_ID = "authorId"
        const val FIELD_POST_ID = "postId"
        const val FIELD_PARENT_ID = "parentId"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_CONTENT = "content"
        const val FIELD_LIKE_COUNT = "likeCount"
    }
}
