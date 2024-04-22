package com.ki960213.kidsandseoul.data.firebase.user

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_FACILITY_CHAT_ROOMS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_GROUP_CHAT_ROOMS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_KIDS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_POSTS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_USERS
import com.ki960213.kidsandseoul.data.firebase.FIRESTORE_MAX_LIMIT
import com.ki960213.kidsandseoul.data.firebase.chatroom.FacilityChatRoomDocument
import com.ki960213.kidsandseoul.data.firebase.chatroom.GroupChatRoomDocument
import com.ki960213.kidsandseoul.data.firebase.post.PostDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    suspend fun isExist(userId: String): Boolean = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .get()
            .await()
            .exists()
    }

    suspend fun createUser(
        userId: String,
        name: String,
        administrativeDongId: Long,
    ) = withContext(Dispatchers.IO) {
        val document = UserDocument(
            id = userId,
            name = name,
            administrativeDongId = administrativeDongId,
        )
        db.collection(COLLECTION_USERS)
            .document(userId)
            .set(document)
    }

    suspend fun fetchUsers(userIds: List<String>): List<UserDocument> =
        withContext(Dispatchers.IO) {
            userIds.chunked(FIRESTORE_MAX_LIMIT)
                .flatMap { ids ->
                    db.collection(COLLECTION_USERS)
                        .whereIn(UserDocument.FIELD_ID, ids)
                        .get()
                        .await()
                        .toObjects()
                }
        }

    fun getUser(userId: String): Flow<UserDocument?> = db.collection(COLLECTION_USERS)
        .document(userId)
        .snapshots()
        .map { it.toObject<UserDocument>() }
        .flowOn(Dispatchers.IO)

    suspend fun changeName(userId: String, name: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .update(UserDocument.FIELD_NAME, name)
    }

    suspend fun changeProfileImageUrl(
        userId: String,
        profileImageUrl: String,
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .update(UserDocument.FIELD_PROFILE_IMAGE_URL, profileImageUrl)
    }

    suspend fun changeLivingDongId(
        userId: String,
        administrativeDongId: Long,
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .update(UserDocument.FIELD_ADMINISTRATIVE_DONG_ID, administrativeDongId)
    }

    fun getFollowingUserIds(userId: String): Flow<List<String>> = db.collection(COLLECTION_USERS)
        .document(userId)
        .snapshots()
        .map { it.asFollowingUserIds() }
        .flowOn(Dispatchers.IO)

    private fun DocumentSnapshot.asFollowingUserIds(): List<String> =
        this[UserDocument.FIELD_FOLLOWING_USER_IDS].asStringList()

    fun getFollowerIds(userId: String): Flow<List<String>> = db.collection(COLLECTION_USERS)
        .document(userId)
        .snapshots()
        .map { it.asFollowerIds() }
        .flowOn(Dispatchers.IO)

    private fun DocumentSnapshot.asFollowerIds(): List<String> =
        this[UserDocument.FIELD_FOLLOWER_USER_IDS].asStringList()

    private fun Any?.asStringList(): List<String> {
        if (this !is List<*>) return emptyList()
        return filterIsInstance<String>()
    }

    suspend fun follow(userId: String, targetUserId: String) = withContext(Dispatchers.IO) {
        if (userId == targetUserId) return@withContext
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val targetUserRef = db.collection(COLLECTION_USERS).document(targetUserId)
        db.runTransaction { transaction ->
            val followingUserIds = transaction.get(userRef).asFollowingUserIds()
            val followerIds = transaction.get(targetUserRef).asFollowerIds()
            transaction.update(
                userRef,
                UserDocument.FIELD_FOLLOWING_USER_IDS,
                (followingUserIds + targetUserId).distinct(),
            )
            transaction.update(
                targetUserRef,
                UserDocument.FIELD_FOLLOWER_USER_IDS,
                (followerIds + userId).distinct(),
            )
        }
    }

    suspend fun unfollow(userId: String, targetUserId: String) = withContext(Dispatchers.IO) {
        if (userId == targetUserId) return@withContext
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val targetUserRef = db.collection(COLLECTION_USERS).document(targetUserId)
        db.runTransaction { transaction ->
            val followingUserIds = transaction.get(userRef).asFollowingUserIds()
            val followerIds = transaction.get(targetUserRef).asFollowerIds()
            transaction.update(
                userRef,
                UserDocument.FIELD_FOLLOWING_USER_IDS,
                followingUserIds - targetUserId,
            )
            transaction.update(
                targetUserRef,
                UserDocument.FIELD_FOLLOWER_USER_IDS,
                followerIds - userId,
            )
        }
    }

    suspend fun leave(userId: String) = withContext(Dispatchers.IO) {
        val userDocument = fetchUserDocument(userId)
        val kidDocumentRefs = fetchKidDocumentRefs(userId)
        val facilityChatRoomRefs = userDocument?.joinedFacilityChatRoomIds
            ?.takeIf { it.isNotEmpty() }
            ?.let { fetchFacilityChatRoomRefs(it) }
        val groupChatRoomRefs = userDocument?.joinedGroupChatRoomIds
            ?.takeIf { it.isNotEmpty() }
            ?.let { fetchGroupChatRoomRefs(it) }
        val followerUserRefs = userDocument?.followerUserIds
            ?.takeIf { it.isNotEmpty() }
            ?.let { fetchUserRefs(it) }
        val followingUserRefs = userDocument?.followingUserIds
            ?.takeIf { it.isNotEmpty() }
            ?.let { fetchUserRefs(it) }
        db.runTransaction { transaction ->
            kidDocumentRefs.forEach { transaction.delete(it) }
            if (userDocument == null) return@runTransaction
            facilityChatRoomRefs?.also { transaction.exitFacilityChatRooms(it, userId) }
            groupChatRoomRefs?.also { transaction.exitGroupChatRooms(it, userId) }
            followerUserRefs?.also { transaction.cleanUpFollowerUsers(it, userId) }
            followingUserRefs?.also { transaction.cleanUpFollowingUsers(it, userId) }
            transaction.delete(db.collection(COLLECTION_USERS).document(userId))
        }
    }

    private suspend fun fetchUserDocument(userId: String): UserDocument? =
        db.collection(COLLECTION_USERS)
            .document(userId)
            .get()
            .await()
            .toObject()

    private suspend fun fetchKidDocumentRefs(userId: String): List<DocumentReference> =
        db.collection(COLLECTION_USERS)
            .document(userId)
            .collection(COLLECTION_KIDS)
            .get()
            .await()
            .map { it.reference }

    private suspend fun fetchFacilityChatRoomRefs(
        facilityChatRoomIds: List<String>,
    ): List<DocumentReference> = facilityChatRoomIds.chunked(FIRESTORE_MAX_LIMIT)
        .flatMap { ids ->
            db.collection(COLLECTION_FACILITY_CHAT_ROOMS)
                .whereIn(FacilityChatRoomDocument.FIELD_ID, ids)
                .get()
                .await()
                .map { it.reference }
        }

    private fun Transaction.exitFacilityChatRooms(
        facilityChatRoomRefs: List<DocumentReference>,
        userId: String,
    ) {
        facilityChatRoomRefs.forEach { ref ->
            val chatRoomDocument = get(ref).toObject<FacilityChatRoomDocument>() ?: return@forEach
            update(
                ref,
                FacilityChatRoomDocument.FIELD_MEMBER_IDS,
                chatRoomDocument.memberIds - userId
            )
        }
    }

    private suspend fun fetchGroupChatRoomRefs(
        groupChatRoomIds: List<String>,
    ): List<DocumentReference> = groupChatRoomIds.chunked(FIRESTORE_MAX_LIMIT)
        .flatMap { ids ->
            db.collection(COLLECTION_GROUP_CHAT_ROOMS)
                .whereIn(FacilityChatRoomDocument.FIELD_ID, ids)
                .get()
                .await()
                .map { it.reference }
        }

    private fun Transaction.exitGroupChatRooms(
        groupChatRoomRefs: List<DocumentReference>,
        userId: String,
    ) {
        groupChatRoomRefs.forEach { ref ->
            val chatRoomDocument = get(ref).toObject<GroupChatRoomDocument>() ?: return@forEach
            update(
                ref,
                GroupChatRoomDocument.FIELD_MEMBER_IDS,
                chatRoomDocument.memberIds - userId
            )
        }
    }

    private suspend fun fetchUserRefs(userIds: List<String>): List<DocumentReference> =
        db.collection(COLLECTION_USERS)
            .whereIn(UserDocument.FIELD_ID, userIds)
            .get()
            .await()
            .map { it.reference }

    /**
     * 탈퇴하려는 유저가 언팔로우 하는 것이므로 팔로우 하고 있던 유저의 팔로워 유저 아이디 목록을 수정
     */
    private fun Transaction.cleanUpFollowingUsers(
        followingUserRefs: List<DocumentReference>,
        leavingUserId: String,
    ) {
        followingUserRefs.forEach { ref ->
            val userDocument = get(ref).toObject<UserDocument>() ?: return@forEach
            update(
                ref,
                UserDocument.FIELD_FOLLOWER_USER_IDS,
                userDocument.followerUserIds - leavingUserId,
            )
        }
    }

    /**
     * 탈퇴하려는 유저를 팔로우 하는 유저의 팔로잉을 끊어야 하므로
     * 탈퇴하려는 유저를 팔로잉하는 유저의 팔로잉 유저 아이디 목록을 수정
     */
    private fun Transaction.cleanUpFollowerUsers(
        followerUserRefs: List<DocumentReference>,
        leavingUserId: String,
    ) {
        followerUserRefs.forEach { ref ->
            val userDocument = get(ref).toObject<UserDocument>() ?: return@forEach
            update(
                ref,
                UserDocument.FIELD_FOLLOWING_USER_IDS,
                userDocument.followingUserIds - leavingUserId,
            )
        }
    }

    suspend fun addInterestFacilityId(
        userId: String,
        facilityId: Long,
    ) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)

        db.runTransaction { transaction ->
            val userDocument = transaction.get(userRef)
                .toObject<UserDocument>()
                ?: return@runTransaction
            transaction.update(
                userRef,
                UserDocument.FIELD_INTEREST_FACILITY_IDS,
                (userDocument.interestFacilityIds + facilityId).distinct()
            )
        }
    }

    suspend fun deleteInterestFacilityId(
        userId: String,
        facilityId: Long,
    ) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)

        db.runTransaction { transaction ->
            val userDocument = transaction.get(userRef)
                .toObject<UserDocument>()
                ?: return@runTransaction
            transaction.update(
                userRef,
                UserDocument.FIELD_INTEREST_FACILITY_IDS,
                userDocument.interestFacilityIds - facilityId
            )
        }
    }

    suspend fun addLikePostId(
        userId: String,
        postId: String,
    ) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val postRef = db.collection(COLLECTION_POSTS).document(postId)

        db.runTransaction { transaction ->
            val likePostIds = transaction.get(userRef)
                .get(UserDocument.FIELD_LIKE_POST_IDS)
                .asStringList()
                .toSet()
            val post = transaction.get(postRef)
                .toObject<PostDocument>()
                ?: return@runTransaction
            transaction.update(
                userRef,
                UserDocument.FIELD_LIKE_POST_IDS,
                (likePostIds + postId).toList()
            )
            if (postId !in likePostIds) {
                transaction.update(
                    postRef,
                    PostDocument.FIELD_LIKE_COUNT,
                    post.likeCount + 1
                )
            }
        }
    }

    suspend fun deleteLikePostId(
        userId: String,
        postId: String,
    ) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val postRef = db.collection(COLLECTION_POSTS).document(postId)

        db.runTransaction { transaction ->
            val likePostIds = transaction.get(userRef)
                .get(UserDocument.FIELD_LIKE_POST_IDS)
                .asStringList()
                .toSet()
            val post = transaction.get(postRef)
                .toObject<PostDocument>()
                ?: return@runTransaction
            transaction.update(
                userRef,
                UserDocument.FIELD_LIKE_POST_IDS,
                (likePostIds - postId).toList()
            )
            if (postId in likePostIds) {
                transaction.update(
                    postRef,
                    PostDocument.FIELD_LIKE_COUNT,
                    post.likeCount - 1
                )
            }
        }
    }
}
