package com.ki960213.kidsandseoul.data.database.chatroom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {

    @Transaction
    @Query("SELECT * FROM chat_rooms")
    fun getChatRooms(): Flow<List<ChatRoomAndRecentMessageEntity>>

    @Query("SELECT * FROM chat_rooms WHERE id = :chatRoomId")
    fun getChatRoom(chatRoomId: String): Flow<ChatRoomAndRecentMessageEntity>

    @Insert
    suspend fun addChatRoom(chatRoomEntity: ChatRoomEntity)

    @Query("UPDATE chat_rooms SET isNotificationTurnOn = :isTurnOn WHERE id = :chatRoomId")
    suspend fun setIsNotificationTurnOn(chatRoomId: String, isTurnOn: Boolean)

    @Query("DELETE FROM chat_rooms WHERE id = :chatRoomId")
    suspend fun deleteById(chatRoomId: String)
}
