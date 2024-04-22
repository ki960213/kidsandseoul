package com.ki960213.kidsandseoul.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ki960213.kidsandseoul.data.database.chatroom.ChatRoomDao
import com.ki960213.kidsandseoul.data.database.chatroom.ChatRoomEntity
import com.ki960213.kidsandseoul.data.database.message.MessageDao
import com.ki960213.kidsandseoul.data.database.message.MessageEntity

@Database(entities = [MessageEntity::class, ChatRoomEntity::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class KasDatabase : RoomDatabase() {

    abstract fun chatRoomDao(): ChatRoomDao

    abstract fun messageDao(): MessageDao
}
