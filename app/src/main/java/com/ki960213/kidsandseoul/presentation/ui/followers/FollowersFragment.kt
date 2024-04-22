package com.ki960213.kidsandseoul.presentation.ui.followers

import android.os.Bundle
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFollowersBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.followers.users.FollowUserUiState
import com.ki960213.kidsandseoul.presentation.ui.followers.users.FollowUsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : BaseFragment<FragmentFollowersBinding>(R.layout.fragment_followers) {

    private val viewModel: FollowersViewModel by viewModels()

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
        binding.rvFollowersUsers.adapter = FollowUsersAdapter(
            onUserClick = ::navigateToProfile,
            onFollowToggle = viewModel::setFollow
        )
    }

    private fun navigateToProfile(userId: String) {
        findNavController().navigateSafely(
            FollowersFragmentDirections.actionFollowersFragmentToProfileFragment(userId)
        )
    }

    companion object {

        const val KEY_USER_ID = "userId"
    }
}

@BindingAdapter("app:followers_users")
fun RecyclerView.setUsers(users: List<FollowUserUiState>) {
    (adapter as FollowUsersAdapter).submitList(users)
}
