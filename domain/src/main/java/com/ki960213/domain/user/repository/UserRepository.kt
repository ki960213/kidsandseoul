package com.ki960213.domain.user.repository

import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {

    /**
     * 유저 목록 조회
     * @param[userIds] 유저 아이디 목록
     * @return [User] 목록
     */
    suspend fun getUsers(userIds: List<String>): List<User>

    /**
     * 유저 단건 조회
     * @param[userId] 유저 아이디
     * @return [User]의 [Flow]
     */
    fun getUser(userId: String): Flow<User>

    suspend fun changeName(userId: String, name: String)

    suspend fun changeProfileImageUrl(userId: String, imageFile: File)

    suspend fun changeLivingDong(userId: String, administrativeDongId: Long)

    /**
     * 해당 유저가 팔로잉하는 유저 목록 조회
     * @param[userId] 유저 아이디
     * @return [JoinedUser] 목록 [Flow]
     */
    fun getFollowingUsers(userId: String): Flow<List<JoinedUser>>

    /**
     * 해당 유저를 팔로잉하는 유저 목록 조회
     * @param[userId] 유저 아이디
     * @return [JoinedUser] 목록 [Flow]
     */
    fun getFollowers(userId: String): Flow<List<JoinedUser>>

    /**
     * 팔로우 하기
     *
     * 만약 이미 팔로우 되어 있다면 아무 일도 하지 않음
     * @param[userId] 팔로우 할 유저 아이디
     * @param[targetUserId] 팔로우 될 유저 아이디
     */
    suspend fun follow(userId: String, targetUserId: String)

    /**
     * 언팔로우 하기
     *
     * 만약 이미 언팔로우 되어 있다면 아무 일도 하지 않음
     * @param[userId] 언팔로우 할 유저 아이디
     * @param[targetUserId] 언팔로우 당할 유저 아이디
     */
    suspend fun unfollow(userId: String, targetUserId: String)

    /**
     * 회원 탈퇴
     * @param[userId] 유저 아이디
     */
    suspend fun leave(userId: String)
}
