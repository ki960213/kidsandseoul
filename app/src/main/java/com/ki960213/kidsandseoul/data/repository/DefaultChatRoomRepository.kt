package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.chat.model.ChatRoom
import com.ki960213.domain.chat.model.FacilityChatRoom
import com.ki960213.domain.chat.model.GroupChatRoom
import com.ki960213.domain.chat.model.Message
import com.ki960213.domain.chat.model.PrivateChatRoom
import com.ki960213.domain.chat.repository.ChatRoomRepository
import com.ki960213.kidsandseoul.data.database.chatroom.ChatRoomAndRecentMessageEntity
import com.ki960213.kidsandseoul.data.database.chatroom.ChatRoomDao
import com.ki960213.kidsandseoul.data.database.chatroom.ChatRoomEntity
import com.ki960213.kidsandseoul.data.firebase.chatroom.ChatRoomFirestore
import com.ki960213.kidsandseoul.data.firebase.chatroom.FacilityChatRoomDocument
import com.ki960213.kidsandseoul.data.firebase.chatroom.GroupChatRoomDocument
import com.ki960213.kidsandseoul.data.network.facility.FacilityApi
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkFacility
import com.ki960213.kidsandseoul.data.network.fcm.FcmApi
import com.ki960213.kidsandseoul.data.network.fcm.model.NetworkMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DefaultChatRoomRepository @Inject constructor(
    private val chatRoomFirestore: ChatRoomFirestore,
    private val dao: ChatRoomDao,
    private val facilityApi: FacilityApi,
    private val fcmApi: FcmApi,
) : ChatRoomRepository {

    override fun getChatRooms(userId: String): Flow<List<ChatRoom>> = combine(
        chatRoomFirestore.getJoinedFacilityChatRooms(userId),
        chatRoomFirestore.getJoinedGroupChatRooms(userId),
        dao.getChatRooms(),
    ) { facilityChatRoomDocuments, groupChatRoomDocuments, chatRoomAndRecentMessageEntities ->
        val facilityIds = facilityChatRoomDocuments.map { it.facilityId }
        val chatRoomEntities = chatRoomAndRecentMessageEntities.associateBy { it.chatRoom.id }
        val networkFacilities = facilityApi
            .getFacilities(ids = facilityIds)
            .associateBy { it.id }
        listOf(
            createFacilityChatRooms(
                facilityChatRoomDocuments,
                networkFacilities,
                chatRoomEntities,
            ),
            createGroupChatRooms(groupChatRoomDocuments, chatRoomEntities),
            createPrivateChatRooms(chatRoomEntities)
        ).flatten()
    }

    private fun createFacilityChatRooms(
        facilityChatRoomDocuments: List<FacilityChatRoomDocument>,
        networkFacilities: Map<Long, NetworkFacility>,
        chatRoomEntities: Map<String, ChatRoomAndRecentMessageEntity>,
    ): List<FacilityChatRoom> = facilityChatRoomDocuments.mapNotNull { document ->
        val networkFacility = networkFacilities[document.facilityId] ?: return@mapNotNull null
        val entity = chatRoomEntities[document.id] ?: return@mapNotNull null

        FacilityChatRoom(
            id = document.id,
            name = networkFacility.name,
            unreadMessageCount = entity.chatRoom.unreadMessageCount,
            recentMessage = entity.recentMessage.asMessage(),
            isNotificationTurnOn = entity.chatRoom.isNotificationTurnOn,
            memberIds = document.memberIds,
            facilityId = networkFacility.id,
        )
    }

    private fun createGroupChatRooms(
        groupChatRoomDocuments: List<GroupChatRoomDocument>,
        chatRoomEntities: Map<String, ChatRoomAndRecentMessageEntity>,
    ): List<GroupChatRoom> = groupChatRoomDocuments.mapNotNull { document ->
        val entity = chatRoomEntities[document.id] ?: return@mapNotNull null

        GroupChatRoom(
            id = document.id,
            name = document.name,
            unreadMessageCount = entity.chatRoom.unreadMessageCount,
            recentMessage = entity.recentMessage.asMessage(),
            isNotificationTurnOn = entity.chatRoom.isNotificationTurnOn,
            memberIds = document.memberIds,
        )
    }

    private fun createPrivateChatRooms(
        chatRoomEntities: Map<String, ChatRoomAndRecentMessageEntity>,
    ): List<PrivateChatRoom> = chatRoomEntities.values.filter { it.chatRoom.isPrivateChatRoom() }
        .map { it.asPrivateChatRoom() }

    override fun getChatRoom(chatRoomId: String): Flow<ChatRoom> = combine(
        chatRoomFirestore.getFacilityChatRoom(chatRoomId),
        dao.getChatRoom(chatRoomId),
    ) { facilityChatRoomDocument, chatRoomEntity ->
        if (facilityChatRoomDocument != null) {
            return@combine chatRoomEntity.toFacilityChatRoom(
                memberIds = facilityChatRoomDocument.memberIds,
                facilityId = facilityChatRoomDocument.facilityId,
            )
        }
        if (chatRoomEntity.isPrivateChatRoom()) chatRoomEntity.asPrivateChatRoom() else throw NoSuchElementException()
    }

    override suspend fun createGroupChatRoom(memberIds: List<String>, name: String) {
        chatRoomFirestore.createGroupChatRoom(
            memberIds = memberIds,
            name = name,
        )
    }

    override suspend fun send(message: Message, memberIds: List<String>) {
        fcmApi.send(
            userIds = memberIds,
            message = Json.encodeToString(NetworkMessage.from(message))
        )
    }

    override suspend fun sendMessage(content: String, chatRoomId: String, memberIds: List<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun joinFacilityChatRoom(
        userId: String,
        chatRoomId: String,
        chatRoomName: String,
    ) {
        chatRoomFirestore.join(userId = userId, chatRoomId = chatRoomId)
        val entity = ChatRoomEntity(id = chatRoomId, name = chatRoomName)
        dao.addChatRoom(entity)
    }

    override suspend fun leave(userId: String, chatRoomId: String) {
        chatRoomFirestore.leave(userId = userId, chatRoomId = chatRoomId)
        dao.deleteById(chatRoomId)
    }

    override suspend fun turnOnNotification(chatRoomId: String, userId: String) {
        dao.setIsNotificationTurnOn(chatRoomId = chatRoomId, isTurnOn = true)
    }

    override suspend fun turnOffNotification(chatRoomId: String, userId: String) {
        dao.setIsNotificationTurnOn(chatRoomId = chatRoomId, isTurnOn = false)
    }
}
