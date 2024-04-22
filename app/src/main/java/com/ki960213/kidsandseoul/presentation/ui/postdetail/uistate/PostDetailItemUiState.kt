package com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate

sealed interface PostDetailItemUiState {

    val viewType: ViewType

    enum class ViewType {
        POST, COMMENT
    }
}
