package com.ki960213.kidsandseoul.presentation.ui.chatroom.dialog

import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.presentation.common.view.ImagePermissionDialog
import com.ki960213.kidsandseoul.presentation.ui.chatroom.ChatRoomViewModel
import java.io.File

class ChatRoomImagePermissionDialog : ImagePermissionDialog() {

    private val viewModel: ChatRoomViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onImageFileReceive(imageFile: File) {
        viewModel.sendImage(imageFile)
    }
}
