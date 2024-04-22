package com.ki960213.kidsandseoul.presentation.ui

import com.ki960213.domain.auth.model.Authentication

sealed interface MainUiEvent {
    data class NavigateToJoin(val authentication: Authentication) : MainUiEvent
    data object LoginFail : MainUiEvent
    data object ShowLoginDialog : MainUiEvent
}
