package com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate

import com.ki960213.domain.post.model.Post
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.model.User

data class PostDetailUiState(
    val authorName: String?,
    val authorImageUrl: String?,
    val isLike: Boolean,
    val post: Post,
) : PostDetailItemUiState {

    override val viewType: PostDetailItemUiState.ViewType
        get() = PostDetailItemUiState.ViewType.POST

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
