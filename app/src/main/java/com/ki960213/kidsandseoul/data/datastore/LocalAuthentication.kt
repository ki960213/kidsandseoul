package com.ki960213.kidsandseoul.data.datastore

import com.ki960213.domain.auth.model.Authentication
import kotlinx.serialization.Serializable

@Serializable
sealed interface LocalAuthentication {

    @Serializable
    data class Google(val token: String) : LocalAuthentication {

        constructor(google: Authentication.Google) : this(google.token)
    }

    @Serializable
    data class Kakao(val email: String, val password: String) : LocalAuthentication {

        constructor(kakao: Authentication.Kakao) : this(kakao.email, kakao.password)
    }

    companion object {

        fun from(authentication: Authentication): LocalAuthentication = when (authentication) {
            is Authentication.Google -> Google(authentication)
            is Authentication.Kakao -> Kakao(authentication)
        }
    }
}
