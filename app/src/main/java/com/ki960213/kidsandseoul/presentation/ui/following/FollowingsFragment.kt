package com.ki960213.kidsandseoul.presentation.ui.following

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFollowingsBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.followers.users.FollowUsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingsFragment : BaseFragment<FragmentFollowingsBinding>(R.layout.fragment_followings) {

    private val viewModel: FollowingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupFollowUsersRecyclerView()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
    }

    private fun setupFollowUsersRecyclerView() {
        binding.rvFollowingsUsers.adapter = FollowUsersAdapter(
            onUserClick = ::navigateToProfile,
            onFollowToggle = viewModel::setFollow
        )
    }

    private fun navigateToProfile(userId: String) {
        findNavController().navigateSafely(
            FollowingsFragmentDirections.actionFollowingsFragmentToProfileFragment(userId)
        )
    }

    companion object {

        const val KEY_USER_ID = "userId"
    }
}
