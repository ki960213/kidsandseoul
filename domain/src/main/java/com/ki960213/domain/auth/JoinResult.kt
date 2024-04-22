package com.ki960213.domain.auth

sealed interface JoinResult {
    data object Fail : JoinResult
    data object Success : JoinResult
}
