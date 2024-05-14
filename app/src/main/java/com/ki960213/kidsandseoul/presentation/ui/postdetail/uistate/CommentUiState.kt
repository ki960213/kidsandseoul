package com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate

import com.ki960213.domain.comment.model.Comment
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.model.User

data class CommentUiState(
    val authorName: String?,
    val authorImageUrl: String?,
    val isDeletable: Boolean,
    val comment: Comment,
) : PostDetailItemUiState {

    override val viewType: PostDetailItemUiState.ViewType
        get() = PostDetailItemUiState.ViewType.COMMENT

    constructor(comment: Comment, author: User, isDeletable: Boolean) : this(
        authorName = when (author) {
            is JoinedUser -> author.name
            LeavedUser -> null
        },
        authorImageUrl = when (author) {
            is JoinedUser -> author.name
            LeavedUser -> null
        },
        isDeletable = isDeletable,
        comment = comment,
    )
}
