package com.ki960213.kidsandseoul.data.firebase.user

import androidx.annotation.Keep
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.kidsandseoul.data.firebase.Document
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UserDocument(
    val id: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val administrativeDongId: Long = -1,
    val interestFacilityIds: List<Long> = emptyList(),
    val interestAdministrativeDongIds: List<Long> = emptyList(),
    val followingUserIds: List<String> = emptyList(),
    val followerUserIds: List<String> = emptyList(),
    val likeReviewIds: List<String> = emptyList(),
    val likePostIds: List<String> = emptyList(),
    val selectedKidId: String = "",
) : Document {

    override fun isValid(): Boolean = id.isNotBlank()

    fun toUser(administrativeDongs: Map<Long, AdministrativeDong>) = JoinedUser(
        id = id,
        name = name,
        profileImageUrl = profileImageUrl,
        livingDong = administrativeDongs[administrativeDongId]
            ?: throw AssertionError("행정동 초기화 잘못한듯"),
        followingIds = followingUserIds.toSet(),
        followerIds = followerUserIds.toSet(),
        interestFacilityIds = interestFacilityIds.toSet(),
        likePostIds = likePostIds.toSet(),
    )
}
