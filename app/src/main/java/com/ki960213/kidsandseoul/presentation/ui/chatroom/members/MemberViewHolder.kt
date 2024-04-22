package com.ki960213.kidsandseoul.presentation.ui.chatroom.members

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemChatRoomMemberBinding

class MemberViewHolder(
    parent: ViewGroup,
    onMemberClick: (memberId: String) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room_member, parent, false)
) {

    private val binding = ItemChatRoomMemberBinding.bind(itemView)

    init {
        binding.onMemberClick = onMemberClick
    }

    fun bind(user: JoinedUser) {
        binding.user = user
    }
}
