package com.ki960213.kidsandseoul.presentation.ui.chatroom.messages

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder.InfoMessageViewHolder
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder.MessageViewHolder
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder.MyImageMessageViewHolder
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder.MyTextMessageViewHolder
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder.OtherImageMessageViewHolder
import com.ki960213.kidsandseoul.presentation.ui.chatroom.messages.viewholder.OtherTextMessageViewHolder

class MessagesAdapter(
    private val onProfileClick: (authorId: String) -> Unit,
    private val onMessageLongClick: () -> Boolean,
) : ListAdapter<MessageUiState, MessageViewHolder>(MessageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder =
        when (viewType) {
            MY_TEXT_MESSAGE_TYPE -> MyTextMessageViewHolder(parent)
            MY_IMAGE_MESSAGE_TYPE -> MyImageMessageViewHolder(parent)
            OTHER_TEXT_MESSAGE_TYPE -> OtherTextMessageViewHolder(
                parent = parent,
                onProfileClick = onProfileClick,
                onMessageLongClick = onMessageLongClick,
            )

            OTHER_IMAGE_MESSAGE_TYPE -> OtherImageMessageViewHolder(
                parent = parent,
                onProfileClick = onProfileClick,
                onMessageLongClick = onMessageLongClick,
            )

            INFO_MESSAGE_TYPE -> InfoMessageViewHolder(parent)
            else -> throw AssertionError("${viewType}은 잘못된 뷰타입입니다.")
        }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is MyTextMessageUiState -> MY_TEXT_MESSAGE_TYPE
        is MyImageMessageUiState -> MY_IMAGE_MESSAGE_TYPE
        is OtherTextMessageUiState -> OTHER_TEXT_MESSAGE_TYPE
        is OtherImageMessageUiState -> OTHER_IMAGE_MESSAGE_TYPE
        is DateUiState -> INFO_MESSAGE_TYPE
        is InOutUiState -> INFO_MESSAGE_TYPE
    }

    companion object {

        private const val MY_TEXT_MESSAGE_TYPE = 0
        private const val MY_IMAGE_MESSAGE_TYPE = 1
        private const val OTHER_TEXT_MESSAGE_TYPE = 2
        private const val OTHER_IMAGE_MESSAGE_TYPE = 3
        private const val INFO_MESSAGE_TYPE = 4
    }
}
