package com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap.uistate

sealed interface FacilitiesMapUiEvent {
    data object KeywordTooShort : FacilitiesMapUiEvent
    data object NotFoundFacilities : FacilitiesMapUiEvent
}
