package com.ki960213.kidsandseoul.presentation.ui.chatroom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentChatRoomBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.util.onImagePermissionCompat
import com.ki960213.kidsandseoul.presentation.common.util.toFile
import com.ki960213.kidsandseoul.presentation.ui.chatroom.dialog.ChatRoomImagePermissionDialog
import com.ki960213.kidsandseoul.presentation.ui.chatroom.dialog.ExitConfirmDialog
import com.ki960213.kidsandseoul.presentation.ui.chatroom.dialog.MessageReportConfirmDialog
import com.ki960213.kidsandseoul.presentation.ui.chatroom.members.MembersAdapter
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MessageUiState
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.MessagesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val viewModel: ChatRoomViewModel by viewModels()

    private val imagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) navigateToGallery()
    }

    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageUri = it.data?.data ?: return@registerForActivityResult
                val imageFile = imageUri.toFile(requireContext())
                viewModel.sendImage(imageFile = imageFile)
            }
        }

    private fun navigateToGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        albumLauncher.launch(Intent.createChooser(intent, "갤러리 종류를 선택하세요."))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDateBinding()
        setupMessagesRecyclerView()
        setupMembersRecyclerView()
    }

    private fun setupDateBinding() {
        binding.onBackButtonClick = { findNavController().popBackStack() }
        binding.onImageAddButtonClick = ::startToSendImage
        binding.onMoreButtonClick = { binding.dlChatRoomMore.openDrawer(GravityCompat.END) }
        binding.onSendButtonClick = ::handleSendButtonClick
        binding.onExitButtonClick = ::showExitConfirmDialog
    }

    private fun startToSendImage() {
        requireActivity().onImagePermissionCompat(
            onGranted = ::navigateToGallery,
            onShouldShowRequestPermissionRationale = { showNavigateToDetailSettingDialog() },
            onDenied = { imagePermissionLauncher.launch(it) },
        )
    }

    private fun showNavigateToDetailSettingDialog() {
        ChatRoomImagePermissionDialog().show(childFragmentManager, null)
    }

    private fun showReportConfirmDialog() {
        MessageReportConfirmDialog().show(childFragmentManager, null)
    }

    private fun handleSendButtonClick(text: String) {
        viewModel.sendText(text)
        binding.etChatRoomMessageInput.text.clear()
    }

    private fun showExitConfirmDialog() {
        ExitConfirmDialog().show(childFragmentManager, null)
    }

    private fun setupMessagesRecyclerView() {
        binding.rvChatRoomMessages.adapter = MessagesAdapter(
            onProfileClick = ::navigateToProfile,
            onMessageLongClick = ::handleMessageLongClick
        )
    }

    private fun handleMessageLongClick(): Boolean {
        showReportConfirmDialog()
        return false
    }

    private fun setupMembersRecyclerView() {
        binding.rvChatRoomMembers.adapter = MembersAdapter(::navigateToProfile)
    }

    private fun navigateToProfile(authorId: String) {
        findNavController().navigateSafely(
            ChatRoomFragmentDirections.actionChatRoomFragmentToProfileFragment(userId = authorId)
        )
    }

    companion object {

        const val KEY_CHAT_ROOM_ID = "chatRoomId"
    }
}

@BindingAdapter("app:chatRoom_messages")
fun RecyclerView.setMessages(messages: List<MessageUiState>) {
    (adapter as MessagesAdapter).submitList(messages)
}

@BindingAdapter("app:chatRoom_members")
fun RecyclerView.setMembers(members: List<JoinedUser>) {
    (adapter as MembersAdapter).submitList(members)
}
