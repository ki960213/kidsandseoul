package com.ki960213.kidsandseoul.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.ki960213.domain.auth.JoinResult
import com.ki960213.domain.auth.model.Authentication
import com.ki960213.domain.auth.model.LoginResult
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.kidsandseoul.data.datastore.KasPreferencesDataStore
import com.ki960213.kidsandseoul.data.datastore.LocalAuthentication
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.user.UserFirestore
import com.ki960213.kidsandseoul.data.network.fcm.FcmApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAuthRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val auth: FirebaseAuth,
    private val messaging: FirebaseMessaging,
    private val dataStore: KasPreferencesDataStore,
    private val userFirestore: UserFirestore,
    private val fcmApi: FcmApi,
) : AuthRepository {

    override val loginUserId: Flow<String?> = dataStore.authentication
        .map { authentication ->
            when {
                authentication == null -> {
                    auth.signOut()
                    null
                }

                auth.currentUser != null -> auth.currentUser!!.uid
                else -> when (authentication) {
                    is LocalAuthentication.Google -> loginWithGoogle(authentication.token)
                    is LocalAuthentication.Kakao -> loginWithEmail(
                        email = authentication.email,
                        password = authentication.password
                    )
                }?.uid
            }
        }

    override suspend fun login(authentication: Authentication): LoginResult = externalScope.async {
        val currentUser = when (authentication) {
            is Authentication.Google -> loginWithGoogle(authentication.token)
            is Authentication.Kakao -> loginWithEmail(authentication.email, authentication.password)
        } ?: return@async LoginResult.LoginFail

        val isJoined = userFirestore.isExist(currentUser.uid)
        if (!isJoined) return@async LoginResult.NotJoinedUser

        dataStore.updateAuthentication(LocalAuthentication.from(authentication))
        fcmApi.saveToken(currentUser.uid, messaging.token.await())
        return@async LoginResult.LoginSuccess
    }.await()

    private suspend fun loginWithGoogle(token: String): FirebaseUser? =
        withContext(Dispatchers.IO) {
            val credential = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credential)
                .await()
                .user
        }

    private suspend fun loginWithEmail(
        email: String,
        password: String,
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        runCatching {
            auth.signInWithEmailAndPassword(email, password).await()
        }.getOrElse { error ->
            if (error is FirebaseAuthInvalidCredentialsException) null else throw error
        }?.user
    }

    override suspend fun join(
        authentication: Authentication,
        name: String,
        administrativeDongId: Long,
    ): JoinResult {
        val joinedUser = when (authentication) {
            is Authentication.Google -> loginWithGoogle(authentication.token)
            is Authentication.Kakao -> signUpWithEmail(
                email = authentication.email,
                password = authentication.password
            )
        } ?: return JoinResult.Fail
        userFirestore.createUser(
            userId = joinedUser.uid,
            name = name,
            administrativeDongId = administrativeDongId,
        )
        return JoinResult.Success
    }

    private suspend fun signUpWithEmail(
        email: String,
        password: String,
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        auth.createUserWithEmailAndPassword(email, password)
            .await()
            .user
    }

    override suspend fun logout() = externalScope.launch {
        val loginUserId = auth.currentUser?.uid ?: return@launch
        dataStore.removeAuthentication()
        fcmApi.deleteToken(loginUserId, messaging.token.await())
        withContext(Dispatchers.IO) { auth.signOut() }
    }.join()
}
