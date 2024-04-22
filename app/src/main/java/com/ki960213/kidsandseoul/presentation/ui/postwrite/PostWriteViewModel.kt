package com.ki960213.kidsandseoul.presentation.ui.postwrite

import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.post.repository.PostRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostWriteViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
) : BaseViewModel() {

    val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    private val _uiEvent = MutableSharedFlow<PostWriteUiEvent>()
    val uiEvent: SharedFlow<PostWriteUiEvent> = _uiEvent.asSharedFlow()

    val title = MutableStateFlow("")

    val content = MutableStateFlow("")

    val isChanged: Boolean
        get() = title.value.isNotBlank() || content.value.isNotBlank()

    val canRegister: StateFlow<Boolean> = combine(title, content) { title, content ->
        title.isNotBlank() && content.isNotBlank() && content.length <= CONTENT_MAX_LENGTH
    }.viewModelStateIn(initialValue = false)

    fun register() = viewModelScope.launch {
        require(canRegister.value)
        val postId: String = postRepository.createPost(
            authorId = loginUserId.value ?: return@launch,
            title = title.value,
            content = content.value
        )
        _uiEvent.emit(PostWriteUiEvent.RegisterSuccess(postId))
    }

    companion object {

        const val CONTENT_MAX_LENGTH = 500
    }
}
