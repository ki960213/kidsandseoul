package com.ki960213.domain.auth.model

sealed interface LoginResult {
    data object LoginSuccess : LoginResult
    data object NotJoinedUser : LoginResult
    data object LoginFail : LoginResult
}
