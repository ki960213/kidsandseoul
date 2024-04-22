package com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs

import com.ki960213.domain.administrativedong.model.AdministrativeDong

data class AdministrativeDongUiState(
    val isSelected: Boolean,
    val administrativeDong: AdministrativeDong,
)
