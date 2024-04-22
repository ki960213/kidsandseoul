package com.ki960213.domain.user.model

import com.ki960213.domain.administrativedong.model.AdministrativeDong

sealed interface User

data class JoinedUser(
    val id: String,
    val name: String,
    val profileImageUrl: String,
    val livingDong: AdministrativeDong,
    val followerIds: Set<String>,
    val followingIds: Set<String>,
    val interestFacilityIds: Set<Long>,
    val likePostIds: Set<String>,
) : User {

    val followerCount: Int = followerIds.size
    val followingCount: Int = followingIds.size
}

data object LeavedUser : User
