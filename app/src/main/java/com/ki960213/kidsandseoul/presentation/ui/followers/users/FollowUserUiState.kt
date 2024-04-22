package com.ki960213.kidsandseoul.presentation.ui.followers.users

import com.ki960213.domain.user.model.JoinedUser

data class FollowUserUiState(
    val isFollowing: Boolean,
    val isMe: Boolean,
    val user: JoinedUser,
)
