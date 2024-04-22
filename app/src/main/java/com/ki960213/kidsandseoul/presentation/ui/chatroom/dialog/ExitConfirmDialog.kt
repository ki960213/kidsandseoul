package com.ki960213.kidsandseoul.presentation.ui.chatroom.dialog

import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment
import com.ki960213.kidsandseoul.presentation.ui.chatroom.ChatRoomViewModel

class ExitConfirmDialog : ConfirmDialogFragment() {

    private val viewModel: ChatRoomViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val title: String
        get() = "채팅방 나가기"
    override val message: String
        get() = "채팅방을 나가시겠습니까?"

    override fun action() {
        viewModel.leaveChatRoom()
    }
}
