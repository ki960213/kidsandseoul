package com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs

import com.ki960213.domain.administrativedong.model.Borough

data class BoroughUiState(
    val isSelected: Boolean,
    val borough: Borough,
)
