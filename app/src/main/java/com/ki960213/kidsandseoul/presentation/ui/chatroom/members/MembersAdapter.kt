package com.ki960213.kidsandseoul.presentation.ui.chatroom.members

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.domain.user.model.JoinedUser

class MembersAdapter(
    private val onMemberClick: (memberId: String) -> Unit,
) : ListAdapter<JoinedUser, MemberViewHolder>(JoinedUserDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder =
        MemberViewHolder(parent = parent, onMemberClick = onMemberClick)

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
