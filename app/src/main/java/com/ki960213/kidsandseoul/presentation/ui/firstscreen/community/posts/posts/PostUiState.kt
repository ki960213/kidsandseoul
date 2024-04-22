package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts

import com.ki960213.domain.post.model.Post
import com.ki960213.domain.user.model.JoinedUser

data class PostUiState(
    val authorName: String,
    val authorImageUrl: String,
    val isLike: Boolean,
    val post: Post,
) {

    constructor(post: Post, author: JoinedUser, isLike: Boolean) : this(
        authorName = author.name,
        authorImageUrl = author.profileImageUrl,
        isLike = isLike,
        post = post
    )
}
