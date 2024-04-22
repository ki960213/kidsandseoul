package com.ki960213.kidsandseoul.presentation.ui.chatroom

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.chat.model.ChatRoom
import com.ki960213.domain.chat.model.FacilityChatRoom
import com.ki960213.domain.chat.model.GroupChatRoom
import com.ki960213.domain.chat.model.PrivateChatRoom
import com.ki960213.domain.chat.repository.ChatRoomRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MessageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    private val chatRoomId: String = savedStateHandle[ChatRoomFragment.KEY_CHAT_ROOM_ID]
        ?: throw AssertionError()

    val chatRoom: StateFlow<ChatRoom?> = chatRoomRepository.getChatRoom(chatRoomId)
        .viewModelStateIn(initialValue = null)

    val members: StateFlow<List<JoinedUser>> = chatRoom.map { chatRoom ->
        if (chatRoom == null) return@map emptyList()
        when (chatRoom) {
            is FacilityChatRoom -> userRepository.getUsers(chatRoom.memberIds)
            is GroupChatRoom -> throw AssertionError("아직 그룹 채팅 구현 안함.")
            is PrivateChatRoom -> userRepository.getUsers(listOf(chatRoom.interlocutorId))
        }
    }
        .map { it.filterIsInstance<JoinedUser>() }
        .viewModelStateIn(initialValue = emptyList())

    val messages: StateFlow<List<MessageUiState>> = TODO("메세지 불러오기")

    fun sendImage(imageFile: File) = viewModelScope.launch {
        TODO("이미지 보내기 구현")
    }

    fun sendText(text: String) = viewModelScope.launch {
        val chatRoom = chatRoom.value ?: return@launch
        val memberIds = when (chatRoom) {
            is FacilityChatRoom -> chatRoom.memberIds
            is GroupChatRoom -> throw AssertionError("아직 그룹 채팅 구현 안함")
            is PrivateChatRoom -> listOf(chatRoom.interlocutorId)
        }
        chatRoomRepository.sendMessage(
            content = text,
            chatRoomId = chatRoomId,
            memberIds = memberIds,
        )
    }

    fun leaveChatRoom() = viewModelScope.launch {
        val userId = loginUserId.value ?: return@launch
        chatRoomRepository.leave(userId = userId, chatRoomId = chatRoomId)
    }
}
