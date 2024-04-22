package com.ki960213.kidsandseoul.data.firebase.chatroom

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class GroupChatRoomDocument(
    @JvmField @PropertyName(FIELD_ID) val id: String = "",
    @JvmField @PropertyName(FIELD_NAME) val name: String = "",
    @JvmField @PropertyName(FIELD_MEMBER_IDS) val memberIds: List<String> = emptyList(),
) {

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_MEMBER_IDS = "memberIds"
    }
}
