package com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate

import com.ki960213.domain.post.model.Post
import com.ki960213.domain.user.model.JoinedUser

data class PostDetailUiState(
    val authorName: String,
    val authorImageUrl: String,
    val isLike: Boolean,
    val post: Post,
) : PostDetailItemUiState {

    constructor(post: Post, author: JoinedUser, isLike: Boolean) : this(
        authorName = author.name,
        authorImageUrl = author.profileImageUrl,
        isLike = isLike,
        post = post
    )

    override val viewType: PostDetailItemUiState.ViewType
        get() = PostDetailItemUiState.ViewType.POST
}
