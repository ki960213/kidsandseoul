package com.ki960213.domain.auth.model

sealed interface Authentication {
    data class Google(val token: String) : Authentication
    data class Kakao(val email: String, val password: String) : Authentication
}
