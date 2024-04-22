package com.ki960213.kidsandseoul.data.firebase.chatroom

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class FacilityChatRoomDocument(
    @JvmField @PropertyName(FIELD_ID) val id: String = "",
    @JvmField @PropertyName(FIELD_MEMBER_IDS) val memberIds: List<String> = emptyList(),
    @JvmField @PropertyName(FIELD_FACILITY_ID) val facilityId: Long = -1,
) {

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_MEMBER_IDS = "memberIds"
        const val FIELD_FACILITY_ID = "facilityId"
    }
}
