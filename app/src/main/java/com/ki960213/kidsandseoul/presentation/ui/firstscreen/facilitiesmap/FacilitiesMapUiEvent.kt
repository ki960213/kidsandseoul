package com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap

sealed interface FacilitiesMapUiEvent {
    data object KeywordTooShort : FacilitiesMapUiEvent
}
