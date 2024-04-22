package com.ki960213.kidsandseoul.presentation.ui.profile

sealed interface ProfileUiEvent {
    data object LeavedUserFetch : ProfileUiEvent
}
