package com.ki960213.kidsandseoul.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.model.User
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.data.datastore.KasPreferencesDataStore
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.ImageStorage
import com.ki960213.kidsandseoul.data.firebase.user.UserFirestore
import com.ki960213.kidsandseoul.data.network.fcm.FcmApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val firestore: UserFirestore,
    private val administrativeDongRepository: AdministrativeDongRepository,
    private val auth: FirebaseAuth,
    private val preferencesDataStore: KasPreferencesDataStore,
    private val imageStorage: ImageStorage,
    private val fcmApi: FcmApi,
    private val messaging: FirebaseMessaging,
) : UserRepository {

    override suspend fun getUsers(userIds: List<String>): List<User> = toUsers(userIds)

    override fun getUser(userId: String): Flow<User> = firestore.getUser(userId)
        .map { userDocument ->
            if (userDocument == null) return@map LeavedUser
            val administrativeDongs = administrativeDongRepository.getAdministrativeDongs()
            userDocument.toUser(administrativeDongs)
        }

    override suspend fun changeName(userId: String, name: String) {
        firestore.changeName(userId, name)
    }

    override suspend fun changeProfileImageUrl(
        userId: String,
        imageFile: File,
    ) = externalScope.launch {
        val profileImageUrl = imageStorage.uploadImage(userId, imageFile)
        firestore.changeProfileImageUrl(userId, profileImageUrl)
    }.join()

    override suspend fun changeLivingDong(
        userId: String,
        administrativeDongId: Long,
    ) = externalScope.launch {
        firestore.changeLivingDongId(userId, administrativeDongId)
    }.join()

    override fun getFollowingUsers(userId: String): Flow<List<JoinedUser>> =
        firestore.getFollowingUserIds(userId).map(::toUsers)

    override fun getFollowers(userId: String): Flow<List<JoinedUser>> =
        firestore.getFollowerIds(userId).map(::toUsers)

    private suspend fun toUsers(userIds: List<String>): List<JoinedUser> {
        val userDocuments = firestore.fetchUsers(userIds)
        val administrativeDongs = administrativeDongRepository.getAdministrativeDongs()
        return userDocuments.map { it.toUser(administrativeDongs) }
    }

    override suspend fun follow(userId: String, targetUserId: String) = externalScope.launch {
        if (userId == targetUserId) return@launch
        firestore.follow(userId, targetUserId)
    }.join()

    override suspend fun unfollow(userId: String, targetUserId: String) = externalScope.launch {
        if (userId == targetUserId) return@launch
        firestore.unfollow(userId, targetUserId)
    }.join()

    override suspend fun leave(userId: String) = externalScope.launch {
        firestore.leave(userId)
        auth.currentUser?.delete()
        preferencesDataStore.removeAuthentication()
        fcmApi.deleteToken(userId, messaging.token.await())
        // TODO("메시지와 알림까지 다 삭제해야 함.")
    }.join()
}
