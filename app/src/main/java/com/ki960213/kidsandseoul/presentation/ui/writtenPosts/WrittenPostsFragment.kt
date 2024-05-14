package com.ki960213.kidsandseoul.presentation.ui.writtenPosts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentWrittenPostsBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.MainActivity
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.FirstScreenFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WrittenPostsFragment : BaseFragment<FragmentWrittenPostsBinding>(R.layout.fragment_written_posts) {

    private val viewModel: WrittenPostsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupPostsRecyclerView()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
    }

    private fun setupPostsRecyclerView() {
        binding.rvWrittenPosts.adapter = PostsAdapter(
            onPostClick = ::navigateToPostDetail,
            onProfileUiClick = ::navigateToProfile,
        )
    }

    private fun navigateToPostDetail(postId: String) {
        val direction = WrittenPostsFragmentDirections
            .actionWrittenPostsFragmentToPostDetailFragment(postId)
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun navigateToProfile(userId: String) {
        (requireActivity() as MainActivity).navController.navigateSafely(
            FirstScreenFragmentDirections.actionFirstScreenFragmentToProfileFragment(userId)
        )
    }

    companion object {

        const val KEY_WRITTEN_USER_ID = "userId"
    }
}
