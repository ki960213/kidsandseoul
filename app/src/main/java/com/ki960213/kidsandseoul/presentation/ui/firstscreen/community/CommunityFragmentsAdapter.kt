package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.PostsFragment
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.reviews.ReviewsFragment

class CommunityFragmentsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ReviewsFragment()
        else -> PostsFragment()
    }
}
