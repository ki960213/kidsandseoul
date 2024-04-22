package com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.kids

import com.ki960213.domain.kid.model.Kid

sealed interface KidsItemUiState

data class KidUiState(
    val parentId: String,
    val kid: Kid,
) : KidsItemUiState

data object KidAddUiState : KidsItemUiState
