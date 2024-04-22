package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms

import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.chat.model.ChatRoom
import com.ki960213.domain.chat.model.FacilityChatRoom
import com.ki960213.domain.chat.model.PrivateChatRoom
import com.ki960213.domain.chat.repository.ChatRoomRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms.ChatRoomUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms.FacilityChatRoomUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms.PrivateChatRoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRoomRepository: ChatRoomRepository,
) : BaseViewModel() {

    private val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val chatRooms: StateFlow<List<ChatRoom>> = loginUserId.filterNotNull()
        .flatMapLatest { userId ->
            chatRoomRepository.getChatRooms(userId)
        }.viewModelStateIn(initialValue = emptyList())

    val facilityChatRooms: StateFlow<List<ChatRoomUiState>> = chatRooms
        .map { it.filterIsInstance<FacilityChatRoom>() }
        .map { it.sortedByDescending { chatRoom -> chatRoom.recentMessage.createdAt } }
        .map { it.map(::FacilityChatRoomUiState) }
        .viewModelStateIn(initialValue = emptyList())

    val privateChatRooms: StateFlow<List<ChatRoomUiState>> = chatRooms
        .map { it.filterIsInstance<PrivateChatRoom>() }
        .map { it.sortedByDescending { chatRoom -> chatRoom.recentMessage.createdAt } }
        .map { chatRooms ->
            val userIds = chatRooms.map { it.interlocutorId }.distinct()
            val users = userRepository.getUsers(userIds)
                .filterIsInstance<JoinedUser>()
                .associateBy { it.id }

            chatRooms.map {
                PrivateChatRoomUiState(
                    interlocutorImageUrl = users[it.interlocutorId]?.profileImageUrl ?: "",
                    chatRoom = it
                )
            }
        }
        .viewModelStateIn(initialValue = emptyList())

    fun setNotificationSetting(chatRoomId: String, turnOn: Boolean) = viewModelScope.launch {
        val userId = loginUserId.value ?: return@launch
        if (turnOn) {
            chatRoomRepository.turnOnNotification(chatRoomId, userId)
        } else {
            chatRoomRepository.turnOffNotification(chatRoomId, userId)
        }
    }
}
