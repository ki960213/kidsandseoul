package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

sealed interface FacilitiesUiEvent {
    data object KeywordTooShort : FacilitiesUiEvent
}
