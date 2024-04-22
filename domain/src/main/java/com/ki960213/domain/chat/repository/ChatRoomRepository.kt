package com.ki960213.domain.chat.repository

import com.ki960213.domain.chat.model.ChatRoom
import com.ki960213.domain.chat.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRoomRepository {

    /**
     * 유저가 참여한 채팅방 목록 조회
     *
     * @param[userId] 유저 아이디
     * @return [ChatRoom] 목록 [Flow]
     */
    fun getChatRooms(userId: String): Flow<List<ChatRoom>>

    /**
     * @throws[NoSuchElementException] 채팅방이 없는 경우
     */
    fun getChatRoom(chatRoomId: String): Flow<ChatRoom>

    /**
     * 채팅방 만들기
     * @param[memberIds] 채팅방의 멤버 아이디 목록
     * @param[name] 채팅방의 이름
     */
    suspend fun createGroupChatRoom(memberIds: List<String>, name: String)

    /**
     * 단체 채팅방에 메세지 보내기
     * @param[message] [Message]
     * @param[memberIds] 유저 아이디 목록
     */
    suspend fun send(message: Message, memberIds: List<String>)

    /**
     * 채팅방에 메세지 보내기
     * @param[content] 텍스트 메세지, 이미지 URL, inout 메세지 모두 content로 만들어야 함.
     * @param[chatRoomId] 채팅방 아이디
     * @param[memberIds] 채팅방 멤버 아이디 목록
     */
    suspend fun sendMessage(content: String, chatRoomId: String, memberIds: List<String>)

    suspend fun joinFacilityChatRoom(userId: String, chatRoomId: String, chatRoomName: String)

    /**
     * 채팅방에서 유저 없애기
     * @param[userId] 유저 아이디
     * @param[chatRoomId] 채팅방 아이디
     */
    suspend fun leave(userId: String, chatRoomId: String)

    /**
     * 유저의 채팅방 새 메세지 알림 켜기
     * @param[chatRoomId] 채팅방 아이디
     * @param[userId] 유저 아이디
     */
    suspend fun turnOnNotification(chatRoomId: String, userId: String)

    /**
     * 유저의 채팅방 새 메세지 알림 끄기
     * @param[chatRoomId] 채팅방 아이디
     * @param[userId] 유저 아이디
     */
    suspend fun turnOffNotification(chatRoomId: String, userId: String)
}
