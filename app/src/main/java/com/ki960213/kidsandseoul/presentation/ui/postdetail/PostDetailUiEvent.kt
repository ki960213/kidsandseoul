package com.ki960213.kidsandseoul.presentation.ui.postdetail

sealed interface PostDetailUiEvent {
    data object DeletedPostFetch : PostDetailUiEvent
}
