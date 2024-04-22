package com.ki960213.kidsandseoul.data.database

import android.content.Context
import androidx.room.Room
import com.ki960213.kidsandseoul.data.database.chatroom.ChatRoomDao
import com.ki960213.kidsandseoul.data.database.message.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesKasDatabase(
        @ApplicationContext context: Context,
    ): KasDatabase = Room.databaseBuilder(
        context = context,
        klass = KasDatabase::class.java,
        name = "kas-database",
    ).build()

    @Provides
    @Singleton
    fun providesChatRoomDao(
        database: KasDatabase,
    ): ChatRoomDao = database.chatRoomDao()

    @Provides
    @Singleton
    fun providesMessageDao(
        database: KasDatabase,
    ): MessageDao = database.messageDao()
}
