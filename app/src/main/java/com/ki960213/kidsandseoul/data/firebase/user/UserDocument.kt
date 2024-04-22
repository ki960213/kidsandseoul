package com.ki960213.kidsandseoul.data.firebase.user

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.user.model.JoinedUser

@Keep
data class UserDocument(
    @JvmField @PropertyName(FIELD_ID) val id: String = "",
    @JvmField @PropertyName(FIELD_NAME) val name: String = "",
    @JvmField @PropertyName(FIELD_PROFILE_IMAGE_URL) val profileImageUrl: String = "",
    @JvmField @PropertyName(FIELD_ADMINISTRATIVE_DONG_ID) val administrativeDongId: Long = -1,
    @JvmField @PropertyName(FIELD_INTEREST_FACILITY_IDS) val interestFacilityIds: List<Long> = emptyList(),
    @JvmField @PropertyName(FIELD_INTEREST_ADMINISTRATIVE_DONG_IDS) val interestAdministrativeDongIds: List<Long> = emptyList(),
    @JvmField @PropertyName(FIELD_FOLLOWING_USER_IDS) val followingUserIds: List<String> = emptyList(),
    @JvmField @PropertyName(FIELD_FOLLOWER_USER_IDS) val followerUserIds: List<String> = emptyList(),
    @JvmField @PropertyName(FIELD_JOINED_FACILITY_CHAT_ROOM_IDS) val joinedFacilityChatRoomIds: List<String> = emptyList(),
    @JvmField @PropertyName(FIELD_JOINED_GROUP_CHAT_ROOM_IDS) val joinedGroupChatRoomIds: List<String> = emptyList(),
    @JvmField @PropertyName(FIELD_LIKE_REVIEW_IDS) val likeReviewIds: List<String> = emptyList(),
    @JvmField @PropertyName(FIELD_LIKE_POST_IDS) val likePostIds: List<String> = emptyList(),
    @JvmField @PropertyName(FIELD_SELECTED_KID_ID) val selectedKidId: String = "",
) {

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

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_PROFILE_IMAGE_URL = "profileImageUrl"
        const val FIELD_ADMINISTRATIVE_DONG_ID = "administrativeDongId"
        const val FIELD_INTEREST_FACILITY_IDS = "interestFacilityIds"
        const val FIELD_INTEREST_ADMINISTRATIVE_DONG_IDS = "interestAdministrativeDongIds"
        const val FIELD_FOLLOWING_USER_IDS = "followingUserIds"
        const val FIELD_FOLLOWER_USER_IDS = "followerUserIds"
        const val FIELD_JOINED_FACILITY_CHAT_ROOM_IDS = "joinedFacilityChatRoomIds"
        const val FIELD_JOINED_GROUP_CHAT_ROOM_IDS = "joinedGroupChatRoomIds"
        const val FIELD_LIKE_REVIEW_IDS = "likeReviewIds"
        const val FIELD_LIKE_POST_IDS = "likePostIds"
        const val FIELD_SELECTED_KID_ID = "selectedKidId"
    }
}
