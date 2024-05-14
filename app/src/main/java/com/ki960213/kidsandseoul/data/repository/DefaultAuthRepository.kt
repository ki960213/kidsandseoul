package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.auth.model.Authentication
import com.ki960213.domain.auth.model.LoginResult
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.ImageStorage
import com.ki960213.kidsandseoul.data.firebase.user.UserFirestore
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAuthRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val auth: FirebaseAuth,
    private val userFirestore: UserFirestore,
    private val imageStorage: ImageStorage,
) : AuthRepository {

    override val loginUserId: Flow<String?> = auth.authStateChanged.map { it?.uid }

    override suspend fun login(authentication: Authentication): LoginResult =
        externalScope.async {
            val currentUser = when (authentication) {
                is Authentication.Google -> loginWithGoogle(authentication.token)
                is Authentication.Kakao -> loginWithEmail(authentication.email, authentication.password)
            } ?: return@async LoginResult.LoginFail

            val isJoined = userFirestore.isExist(currentUser.uid)
            if (!isJoined) return@async LoginResult.NotJoinedUser

            return@async LoginResult.LoginSuccess
        }.await()

    private suspend fun loginWithGoogle(token: String): FirebaseUser? =
        withContext(Dispatchers.IO) {
            runCatching {
                val credential = GoogleAuthProvider.credential(token, null)
                auth.signInWithCredential(credential)
            }.getOrElse { error ->
                if (error is FirebaseAuthInvalidCredentialsException) null else throw error
            }?.user
        }

    private suspend fun loginWithEmail(
        email: String,
        password: String,
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        runCatching {
            auth.signInWithEmailAndPassword(email, password)
        }.getOrElse { error ->
            if (error is FirebaseAuthInvalidCredentialsException) null else throw error
        }?.user
    }

    override suspend fun join(
        authentication: Authentication,
        name: String,
        administrativeDongId: Long,
    ): Result<Unit> {
        val joinedUser = when (authentication) {
            is Authentication.Google -> loginWithGoogle(authentication.token)
            is Authentication.Kakao -> signUpWithEmail(
                email = authentication.email,
                password = authentication.password
            )
        } ?: return Result.failure(Exception("로그인에 실패했습니다."))
        userFirestore.createUser(
            userId = joinedUser.uid,
            name = name,
            administrativeDongId = administrativeDongId,
        )
        return Result.success(Unit)
    }

    private suspend fun signUpWithEmail(
        email: String,
        password: String,
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        auth.createUserWithEmailAndPassword(email, password).user
    }

    override suspend fun logout() = externalScope.launch {
        withContext(Dispatchers.IO) {
            auth.signOut()
        }
    }.join()

    override suspend fun leave(userId: String): Result<Unit> = externalScope.async {
        runCatching {
            auth.currentUser?.android?.delete()
        }.onFailure {
            logout()
            return@async Result.failure(Exception("회원 탈퇴에 실패했습니다."))
        }
        userFirestore.leave(userId)
        imageStorage.deleteImage(userId)
        return@async Result.success(Unit)
    }.await()
}
