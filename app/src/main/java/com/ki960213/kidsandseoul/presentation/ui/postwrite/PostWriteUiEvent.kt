package com.ki960213.kidsandseoul.presentation.ui.postwrite

sealed interface PostWriteUiEvent {
    data class RegisterSuccess(val postId: String) : PostWriteUiEvent
}
