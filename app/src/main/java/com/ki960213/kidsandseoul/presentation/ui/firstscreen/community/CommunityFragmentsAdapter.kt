package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.chatrooms.ChatRoomsFragment
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.PostsFragment
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.reviews.ReviewsFragment

class CommunityFragmentsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ReviewsFragment()
        1 -> PostsFragment()
        else -> ChatRoomsFragment()
    }
}
