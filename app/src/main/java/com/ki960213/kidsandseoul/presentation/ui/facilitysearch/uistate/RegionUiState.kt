package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate

import com.ki960213.domain.administrativedong.model.AdministrativeDong

sealed interface RegionUiState {

    data class Borough(val borough: com.ki960213.domain.administrativedong.model.Borough) : RegionUiState

    data class Dong(val administrativeDong: AdministrativeDong) : RegionUiState
}

data class SelectableRegionUiState(val isSelected: Boolean, val region: RegionUiState)
