package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.model.User
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.ImageStorage
import com.ki960213.kidsandseoul.data.firebase.user.UserFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val firestore: UserFirestore,
    private val administrativeDongRepository: AdministrativeDongRepository,
    private val imageStorage: ImageStorage,
) : UserRepository {

    override fun getUsers(
        userIds: List<String>,
    ): Flow<List<User>> = administrativeDongRepository.administrativeDongs
        .map { administrativeDongs ->
            firestore.fetchUsers(userIds)
                .map { it.toUser(administrativeDongs) }
        }

    override fun getUser(userId: String): Flow<User> = combine(
        firestore.getUser(userId),
        administrativeDongRepository.administrativeDongs
    ) { userDocument, administrativeDongs ->
        if (userDocument == null) return@combine LeavedUser
        userDocument.toUser(administrativeDongs)
    }

    override suspend fun changeName(userId: String, name: String) {
        firestore.changeName(userId, name)
    }

    override suspend fun changeProfileImageUrl(
        userId: String,
        imageFile: File,
    ) = externalScope.launch {
        val profileImageUrl = imageStorage.uploadImage(
            imageName = userId,
            imageFile = imageFile
        )
        firestore.changeProfileImageUrl(userId, profileImageUrl)
    }.join()

    override suspend fun changeLivingDong(
        userId: String,
        administrativeDongId: Long,
    ) = externalScope.launch {
        firestore.changeLivingDongId(userId, administrativeDongId)
    }.join()

    override fun getFollowingUsers(userId: String): Flow<List<JoinedUser>> = combine(
        firestore.getFollowingUserIds(userId),
        administrativeDongRepository.administrativeDongs,
    ) { userIds, administrativeDongs ->
        val userDocuments = firestore.fetchUsers(userIds)
        userDocuments.map { it.toUser(administrativeDongs) }
    }

    override fun getFollowers(userId: String): Flow<List<JoinedUser>> = combine(
        firestore.getFollowerIds(userId),
        administrativeDongRepository.administrativeDongs,
    ) { userIds, administrativeDongs ->
        val userDocuments = firestore.fetchUsers(userIds)
        userDocuments.map { it.toUser(administrativeDongs) }
    }

    override suspend fun follow(
        userId: String,
        targetUserId: String,
    ) = externalScope.launch {
        if (userId == targetUserId) return@launch
        firestore.follow(userId, targetUserId)
    }.join()

    override suspend fun unfollow(
        userId: String,
        targetUserId: String,
    ) = externalScope.launch {
        if (userId == targetUserId) return@launch
        firestore.unfollow(userId, targetUserId)
    }.join()
}
