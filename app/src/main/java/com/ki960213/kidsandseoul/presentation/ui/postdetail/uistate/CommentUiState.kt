package com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate

import com.ki960213.domain.comment.model.Comment
import com.ki960213.domain.user.model.JoinedUser

data class CommentUiState(
    val authorName: String,
    val authorImageUrl: String,
    val isDeletable: Boolean,
    val comment: Comment,
) : PostDetailItemUiState {

    override val viewType: PostDetailItemUiState.ViewType
        get() = PostDetailItemUiState.ViewType.COMMENT

    constructor(comment: Comment, author: JoinedUser, isDeletable: Boolean) : this(
        authorName = author.name,
        authorImageUrl = author.profileImageUrl,
        isDeletable = isDeletable,
        comment = comment,
    )
}
