package com.ki960213.kidsandseoul.data.firebase.post

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.ki960213.domain.post.model.Post
import com.ki960213.kidsandseoul.data.firebase.toLocalDateTime

@Keep
data class PostDocument(
    @JvmField @PropertyName(FIELD_ID) val id: String = "",
    @JvmField @PropertyName(FIELD_AUTHOR_ID) val authorId: String = "",
    @JvmField @PropertyName(FIELD_TITLE) val title: String = "",
    @JvmField @PropertyName(FIELD_CONTENT) val content: String = "",
    @JvmField @PropertyName(FIELD_CREATED_AT) val createdAt: Timestamp = Timestamp.now(),
    @JvmField @PropertyName(FIELD_UPDATED_AT) val updatedAt: Timestamp = Timestamp.now(),
    @JvmField @PropertyName(FIELD_LIKE_COUNT) val likeCount: Int = 0,
    @JvmField @PropertyName(FIELD_COMMENT_COUNT) val commentCount: Int = 0,
) {

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

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_AUTHOR_ID = "authorId"
        const val FIELD_TITLE = "title"
        const val FIELD_CONTENT = "content"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_LIKE_COUNT = "likeCount"
        const val FIELD_COMMENT_COUNT = "commentCount"
    }
}
