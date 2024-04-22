package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms

import android.os.Bundle
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentChatRoomsBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms.ChatRoomUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.chatrooms.ChatRoomsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomsFragment : BaseFragment<FragmentChatRoomsBinding>(R.layout.fragment_chat_rooms) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        //        setupChatRoomsRecyclerView()
    }

    private fun setupDataBinding() {
        //        binding.viewModel = viewModel
    }

    //    private fun setupChatRoomsRecyclerView() {
    //        binding.rvChatRoomsFacilityChatRooms.adapter = ChatRoomsAdapter(
    //            onChatRoomClick = ::navigateToChatRoom,
    //            onNotificationToggleClick = viewModel::setNotificationSetting,
    //        )
    //        binding.rvChatRoomsPrivateChatRooms.adapter = ChatRoomsAdapter(
    //            onChatRoomClick = ::navigateToChatRoom,
    //            onNotificationToggleClick = viewModel::setNotificationSetting,
    //        )
    //    }

    private fun navigateToChatRoom(chatRoomId: String) {
        TODO("채팅방 상세화면 이동")
    }
}

@BindingAdapter("app:chatRooms_facilityChatRooms")
fun RecyclerView.setFacilityChatRooms(facilityChatRooms: List<ChatRoomUiState>?) {
    if (facilityChatRooms == null) return
    // TODO("널 처리 지워야 함")
    (adapter as ChatRoomsAdapter).submitList(facilityChatRooms)
}

@BindingAdapter("app:chatRooms_privateChatRooms")
fun RecyclerView.setPrivateChatRooms(privateChatRooms: List<ChatRoomUiState>?) {
    if (privateChatRooms == null) return
    // TODO("널 처리 지워야 함")
    (adapter as ChatRoomsAdapter).submitList(privateChatRooms)
}
