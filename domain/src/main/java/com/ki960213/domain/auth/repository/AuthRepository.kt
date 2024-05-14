package com.ki960213.domain.auth.repository

import com.ki960213.domain.auth.model.Authentication
import com.ki960213.domain.auth.model.LoginResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val loginUserId: Flow<String?>

    /**
     * 로그인 하기
     * @param[authentication] [Authentication]
     * @return 가입하지 않은 회원일 경우 false 반환
     */
    suspend fun login(authentication: Authentication): LoginResult

    suspend fun join(
        authentication: Authentication,
        name: String,
        administrativeDongId: Long,
    ): Result<Unit>

    /**
     * 로그아웃 하기
     */
    suspend fun logout()

    suspend fun leave(userId: String): Result<Unit>
}
