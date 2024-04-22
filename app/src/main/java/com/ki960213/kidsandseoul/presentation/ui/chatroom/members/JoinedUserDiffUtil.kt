package com.ki960213.kidsandseoul.presentation.ui.chatroom.members

import androidx.recyclerview.widget.DiffUtil
import com.ki960213.domain.user.model.JoinedUser

class JoinedUserDiffUtil : DiffUtil.ItemCallback<JoinedUser>() {

    override fun areItemsTheSame(oldItem: JoinedUser, newItem: JoinedUser): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: JoinedUser, newItem: JoinedUser): Boolean =
        oldItem == newItem
}
