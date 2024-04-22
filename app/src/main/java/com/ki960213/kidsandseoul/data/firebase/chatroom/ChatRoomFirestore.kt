package com.ki960213.kidsandseoul.data.firebase.chatroom

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_FACILITY_CHAT_ROOMS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_GROUP_CHAT_ROOMS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_USERS
import com.ki960213.kidsandseoul.data.firebase.user.UserDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRoomFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getJoinedFacilityChatRooms(
        userId: String,
    ): Flow<List<FacilityChatRoomDocument>> = db.collection(COLLECTION_USERS)
        .document(userId)
        .snapshots()
        .map { it.asJoinedFacilityChatRoomIds() }
        .flatMapLatest { it.asFacilityChatRoomsFlow() }
        .flowOn(Dispatchers.IO)

    private fun DocumentSnapshot.asJoinedFacilityChatRoomIds(): List<String> =
        this[UserDocument.FIELD_JOINED_FACILITY_CHAT_ROOM_IDS].asStringList()

    private fun List<String>.asFacilityChatRoomsFlow(): Flow<List<FacilityChatRoomDocument>> =
        db.collection(COLLECTION_FACILITY_CHAT_ROOMS)
            .whereIn(FacilityChatRoomDocument.FIELD_ID, this)
            .snapshots()
            .map { it.toObjects<FacilityChatRoomDocument>() }

    fun getFacilityChatRoom(
        chatRoomId: String
    ): Flow<FacilityChatRoomDocument?> = db.collection(COLLECTION_FACILITY_CHAT_ROOMS)
        .document(chatRoomId)
        .snapshots()
        .map { it.toObject<FacilityChatRoomDocument>() }
        .flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getJoinedGroupChatRooms(
        userId: String,
    ): Flow<List<GroupChatRoomDocument>> = db.collection(COLLECTION_USERS)
        .document(userId)
        .snapshots()
        .map { it.asJoinedGroupChatRoomIds() }
        .flatMapLatest { it.asGroupChatRoomsFlow() }
        .flowOn(Dispatchers.IO)

    private fun DocumentSnapshot.asJoinedGroupChatRoomIds(): List<String> =
        this[UserDocument.FIELD_JOINED_GROUP_CHAT_ROOM_IDS].asStringList()

    private fun Any?.asStringList(): List<String> {
        if (this !is List<*>) return emptyList()
        return filterIsInstance<String>()
    }

    private fun List<String>.asGroupChatRoomsFlow(): Flow<List<GroupChatRoomDocument>> =
        db.collection(COLLECTION_GROUP_CHAT_ROOMS)
            .whereIn(GroupChatRoomDocument.FIELD_ID, this)
            .snapshots()
            .map { it.toObjects<GroupChatRoomDocument>() }

    suspend fun createGroupChatRoom(
        memberIds: List<String>,
        name: String,
    ) = withContext(Dispatchers.IO) {
        val groupChatRoomRef = db.collection(COLLECTION_GROUP_CHAT_ROOMS).document()
        val data = GroupChatRoomDocument(
            id = groupChatRoomRef.id,
            name = name,
            memberIds = memberIds,
        )
        db.collection(COLLECTION_GROUP_CHAT_ROOMS)
            .document(groupChatRoomRef.id)
            .set(data)
    }

    suspend fun join(userId: String, chatRoomId: String) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val facilityChatRoomRef = db.collection(COLLECTION_FACILITY_CHAT_ROOMS).document(chatRoomId)
        val groupChatRoomRef = db.collection(COLLECTION_GROUP_CHAT_ROOMS).document(chatRoomId)

        db.runTransaction { transaction ->
            val userDocument = transaction.get(userRef)
                .toObject<UserDocument>()
                ?: return@runTransaction
            val facilityChatRoomDocument = transaction.get(facilityChatRoomRef)
                .toObject<FacilityChatRoomDocument>()
            val groupChatRoomDocument = transaction.get(groupChatRoomRef)
                .toObject<GroupChatRoomDocument>()

            if (facilityChatRoomDocument != null) {
                transaction.update(
                    facilityChatRoomRef,
                    FacilityChatRoomDocument.FIELD_MEMBER_IDS,
                    (facilityChatRoomDocument.memberIds + userId).toSet(),
                )
                transaction.update(
                    userRef,
                    UserDocument.FIELD_JOINED_FACILITY_CHAT_ROOM_IDS,
                    (userDocument.joinedFacilityChatRoomIds + facilityChatRoomDocument.id).toSet()
                )
            }
            if (groupChatRoomDocument != null) {
                transaction.update(
                    groupChatRoomRef,
                    GroupChatRoomDocument.FIELD_MEMBER_IDS,
                    (groupChatRoomDocument.memberIds + userId).toSet(),
                )
                transaction.update(
                    userRef,
                    UserDocument.FIELD_JOINED_GROUP_CHAT_ROOM_IDS,
                    (userDocument.joinedGroupChatRoomIds + groupChatRoomDocument.id).toSet()
                )
            }
        }
    }

    suspend fun leave(userId: String, chatRoomId: String) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val facilityChatRoomRef = db.collection(COLLECTION_FACILITY_CHAT_ROOMS).document(chatRoomId)
        val groupChatRoomRef = db.collection(COLLECTION_GROUP_CHAT_ROOMS).document(chatRoomId)

        db.runTransaction { transaction ->
            val userDocument = transaction.get(userRef)
                .toObject<UserDocument>()
                ?: return@runTransaction
            val facilityChatRoomDocument = transaction.get(facilityChatRoomRef)
                .toObject<FacilityChatRoomDocument>()
            val groupChatRoomDocument = transaction.get(groupChatRoomRef)
                .toObject<GroupChatRoomDocument>()

            if (facilityChatRoomDocument != null) {
                transaction.update(
                    facilityChatRoomRef,
                    FacilityChatRoomDocument.FIELD_MEMBER_IDS,
                    facilityChatRoomDocument.memberIds - userId,
                )
                transaction.update(
                    userRef,
                    UserDocument.FIELD_JOINED_FACILITY_CHAT_ROOM_IDS,
                    userDocument.joinedFacilityChatRoomIds - facilityChatRoomDocument.id
                )
            }
            if (groupChatRoomDocument != null) {
                transaction.update(
                    groupChatRoomRef,
                    GroupChatRoomDocument.FIELD_MEMBER_IDS,
                    groupChatRoomDocument.memberIds - userId,
                )
                transaction.update(
                    userRef,
                    UserDocument.FIELD_JOINED_GROUP_CHAT_ROOM_IDS,
                    userDocument.joinedGroupChatRoomIds - groupChatRoomDocument.id
                )
            }
        }
    }
}
