package com.ki960213.kidsandseoul.data.firebase.user

import com.ki960213.kidsandseoul.data.firebase.COLLECTION_KIDS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_POSTS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_USERS
import com.ki960213.kidsandseoul.data.firebase.FIRESTORE_MAX_LIMIT
import com.ki960213.kidsandseoul.data.firebase.documentFlow
import com.ki960213.kidsandseoul.data.firebase.fetchDocument
import com.ki960213.kidsandseoul.data.firebase.fetchDocuments
import com.ki960213.kidsandseoul.data.firebase.post.PostDocument
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.Transaction
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    suspend fun isExist(userId: String): Boolean =
        withContext(Dispatchers.IO) {
            db.collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .exists
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
                        .where { UserDocument::id.name inArray ids }
                        .fetchDocuments()
                }
        }

    fun getUser(userId: String): Flow<UserDocument?> =
        db.collection(COLLECTION_USERS)
            .document(userId)
            .documentFlow<UserDocument>()
            .flowOn(Dispatchers.IO)

    suspend fun changeName(
        userId: String,
        name: String,
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .update(UserDocument::name.name to name)
    }

    suspend fun changeProfileImageUrl(
        userId: String,
        profileImageUrl: String,
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .update(UserDocument::profileImageUrl.name to profileImageUrl)
    }

    suspend fun changeLivingDongId(
        userId: String,
        administrativeDongId: Long,
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .update(UserDocument::administrativeDongId.name to administrativeDongId)
    }

    fun getInterestFacilityIds(userId: String): Flow<List<Long>> =
        db.collection(COLLECTION_USERS)
            .document(userId)
            .documentFlow<UserDocument>()
            .map { it?.interestFacilityIds ?: emptyList() }
            .flowOn(Dispatchers.IO)

    fun getFollowingUserIds(userId: String): Flow<List<String>> =
        db.collection(COLLECTION_USERS)
            .document(userId)
            .documentFlow<UserDocument>()
            .map { it?.followingUserIds ?: emptyList() }
            .flowOn(Dispatchers.IO)

    fun getFollowerIds(userId: String): Flow<List<String>> =
        db.collection(COLLECTION_USERS)
            .document(userId)
            .documentFlow<UserDocument>()
            .map { it?.followerUserIds ?: emptyList() }
            .flowOn(Dispatchers.IO)

    suspend fun follow(
        userId: String,
        targetUserId: String,
    ) = withContext(Dispatchers.IO) {
        if (userId == targetUserId) return@withContext
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val targetUserRef = db.collection(COLLECTION_USERS).document(targetUserId)
        db.runTransaction {
            val user = get(userRef).fetchDocument<UserDocument>() ?: return@runTransaction
            val targetUser = get(targetUserRef).fetchDocument<UserDocument>() ?: return@runTransaction
            update(
                userRef,
                UserDocument::followingUserIds.name to (user.followingUserIds + targetUserId).distinct(),
            )
            update(
                targetUserRef,
                UserDocument::followerUserIds.name to (targetUser.followerUserIds + userId).distinct()
            )
        }
    }

    suspend fun unfollow(userId: String, targetUserId: String) {
        if (userId == targetUserId) return
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val targetUserRef = db.collection(COLLECTION_USERS).document(targetUserId)
        db.runTransaction {
            val user = get(userRef).fetchDocument<UserDocument>() ?: return@runTransaction
            val targetUser = get(targetUserRef).fetchDocument<UserDocument>() ?: return@runTransaction
            update(
                userRef,
                UserDocument::followingUserIds.name to user.followingUserIds - targetUserId,
            )
            update(
                targetUserRef,
                UserDocument::followerUserIds.name to targetUser.followerUserIds - userId,
            )
        }
    }

    suspend fun leave(userId: String) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val kidDocumentRefs = fetchKidDocumentRefs(userId)
        db.runTransaction {
            val userDocument = get(userRef).fetchDocument<UserDocument>() ?: return@runTransaction
            val followerUsers = fetchUsers(userDocument.followerUserIds)
            val followingUsers = fetchUsers(userDocument.followingUserIds)
            val followerUserRefs = followerUsers.map { db.collection(COLLECTION_USERS).document(it.id) }
            val followingUserRefs = followingUsers.map { db.collection(COLLECTION_USERS).document(it.id) }
            kidDocumentRefs.forEach { delete(it) }
            unfollowFollowersOfLeavingUser(followerUserRefs, followerUsers, userId)
            unfollowFollowingUsersOfLeavingUser(followingUserRefs, followingUsers, userId)
            delete(db.collection(COLLECTION_USERS).document(userId))
        }
    }

    private suspend fun fetchKidDocumentRefs(userId: String): List<DocumentReference> =
        db.collection(COLLECTION_USERS)
            .document(userId)
            .collection(COLLECTION_KIDS)
            .get()
            .documents
            .map { it.reference }

    /**
     * 탈퇴하려는 유저가 언팔로우 하는 것이므로 팔로우 하고 있던 유저의 팔로워 유저 아이디 목록을 수정
     */
    private fun Transaction.unfollowFollowingUsersOfLeavingUser(
        followingUserRefs: List<DocumentReference>,
        followingUsers: List<UserDocument>,
        leavingUserId: String,
    ) {
        followingUserRefs.zip(followingUsers).forEach { (userRef, userDocument) ->
            val newFollowerUserIds = userDocument.followerUserIds - leavingUserId
            update(
                userRef,
                UserDocument::followerUserIds.name to newFollowerUserIds,
            )
        }
    }

    /**
     * 탈퇴하려는 유저를 팔로우 하는 유저의 팔로잉을 끊어야 하므로
     * 탈퇴하려는 유저를 팔로잉하는 유저의 팔로잉 유저 아이디 목록을 수정
     */
    private fun Transaction.unfollowFollowersOfLeavingUser(
        followerRefs: List<DocumentReference>,
        followers: List<UserDocument>,
        leavingUserId: String,
    ) {
        followerRefs.zip(followers).forEach { (userRef, userDocument) ->
            val newFollowingUserIds = userDocument.followingUserIds - leavingUserId
            update(
                userRef,
                UserDocument::followingUserIds.name to newFollowingUserIds,
            )
        }
    }

    suspend fun addInterestFacilityId(
        userId: String,
        facilityId: Long,
    ) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)

        db.runTransaction {
            val userDocument = get(userRef).fetchDocument<UserDocument>() ?: return@runTransaction
            val newInterestFacilityIds = (userDocument.interestFacilityIds + facilityId).distinct()
            update(
                userRef,
                UserDocument::interestFacilityIds.name to newInterestFacilityIds,
            )
        }
    }

    suspend fun deleteInterestFacilityId(
        userId: String,
        facilityId: Long,
    ) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)

        db.runTransaction {
            val userDocument = get(userRef).fetchDocument<UserDocument>() ?: return@runTransaction
            val newInterestFacilityIds = userDocument.interestFacilityIds - facilityId
            update(
                userRef,
                UserDocument::interestFacilityIds.name to newInterestFacilityIds,
            )
        }
    }

    suspend fun addLikePostId(
        userId: String,
        postId: String,
    ) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTION_USERS).document(userId)
        val postRef = db.collection(COLLECTION_POSTS).document(postId)

        db.runTransaction {
            val user = get(userRef).fetchDocument<UserDocument>() ?: return@runTransaction
            val post = get(postRef).fetchDocument<PostDocument>() ?: return@runTransaction
            val newLikePostIds = (user.likePostIds + postId).distinct()
            update(
                userRef,
                UserDocument::likePostIds.name to newLikePostIds
            )
            if (postId !in user.likePostIds) {
                val newLikeCount = post.likeCount + 1
                update(
                    postRef,
                    PostDocument::likeCount.name to newLikeCount
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

        db.runTransaction {
            val user = get(userRef).fetchDocument<UserDocument>() ?: return@runTransaction
            val post = get(postRef).fetchDocument<PostDocument>() ?: return@runTransaction
            val newLikePostIds = user.likePostIds - postId
            update(
                userRef,
                UserDocument::likePostIds.name to newLikePostIds,
            )
            if (postId in user.likePostIds) {
                update(
                    postRef,
                    PostDocument::likeCount.name to post.likeCount - 1
                )
            }
        }
    }
}
