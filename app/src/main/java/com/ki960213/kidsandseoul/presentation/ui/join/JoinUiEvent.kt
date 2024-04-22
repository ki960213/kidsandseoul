package com.ki960213.kidsandseoul.presentation.ui.join

sealed interface JoinUiEvent {
    data object JoinFail : JoinUiEvent
}
