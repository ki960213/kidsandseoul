package com.ki960213.kidsandseoul.presentation.ui.join

import android.os.Parcelable
import com.ki960213.domain.auth.model.Authentication
import kotlinx.parcelize.Parcelize

sealed interface AuthenticationArgs : Parcelable {

    @Parcelize
    data class Google(val token: String) : AuthenticationArgs {

        override fun asAuthentication(): Authentication = Authentication.Google(token)
    }

    @Parcelize
    data class Kakao(val email: String, val password: String) : AuthenticationArgs {

        override fun asAuthentication(): Authentication = Authentication.Kakao(email, password)
    }

    fun asAuthentication(): Authentication
}

fun Authentication.asArgs(): AuthenticationArgs = when (this) {
    is Authentication.Google -> AuthenticationArgs.Google(token)
    is Authentication.Kakao -> AuthenticationArgs.Kakao(email, password)
}
