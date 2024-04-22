package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentCommunityBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCommunitiesViewPager()
        setupCommunitiesTapLayout()
    }

    private fun setupCommunitiesTapLayout() {
        TabLayoutMediator(binding.tlCommunity, binding.vpCommunity) { tab, position ->
            tab.text = when (position) {
                0 -> "후기"
                1 -> "게시글"
                else -> "채팅"
            }
        }.attach()
    }

    private fun setupCommunitiesViewPager() {
        binding.vpCommunity.adapter = CommunityFragmentsAdapter(this)
    }
}
