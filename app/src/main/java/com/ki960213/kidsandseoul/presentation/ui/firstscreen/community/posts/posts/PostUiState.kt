package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts

import com.ki960213.domain.post.model.Post
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.model.User

data class PostUiState(
    val authorName: String?,
    val authorImageUrl: String?,
    val isLike: Boolean,
    val post: Post,
) {

    constructor(post: Post, author: User, isLike: Boolean) : this(
        authorName = when (author) {
            is JoinedUser -> author.name
            LeavedUser -> null
        },
        authorImageUrl = when (author) {
            is JoinedUser -> author.profileImageUrl
            LeavedUser -> null
        },
        isLike = isLike,
        post = post
    )
}
