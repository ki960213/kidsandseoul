package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts

import android.os.Bundle
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentPostsBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.MainActivity
import com.ki960213.kidsandseoul.presentation.ui.MainViewModel
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.FirstScreenFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts.PostUiState
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.posts.posts.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment : BaseFragment<FragmentPostsBinding>(R.layout.fragment_posts) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val postsViewModel: PostsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupPostsRecyclerView()
    }

    private fun setupDataBinding() {
        binding.viewModel = postsViewModel
        binding.onWriteButtonClick = ::handlePostWriteButtonClick
    }

    private fun handlePostWriteButtonClick() {
        if (postsViewModel.loginUserId.value != null) {
            navigateToPostWrite()
        } else {
            mainViewModel.dispatchLoginEvent()
        }
    }

    private fun navigateToPostWrite() {
        (requireActivity() as MainActivity).navController.navigateSafely(
            FirstScreenFragmentDirections.actionFirstScreenFragmentToPostWriteFragment()
        )
    }

    private fun setupPostsRecyclerView() {
        binding.rvPosts.adapter = PostsAdapter(
            onPostClick = ::navigateToPostDetail,
            onProfileUiClick = ::navigateToProfile,
        )
    }

    private fun navigateToPostDetail(postId: String) {
        (requireActivity() as MainActivity).navController.navigateSafely(
            FirstScreenFragmentDirections.actionFirstScreenFragmentToPostDetailFragment(postId)
        )
    }

    private fun navigateToProfile(userId: String) {
        (requireActivity() as MainActivity).navController.navigateSafely(
            FirstScreenFragmentDirections.actionFirstScreenFragmentToProfileFragment(userId)
        )
    }
}

@BindingAdapter("app:posts_posts")
fun RecyclerView.setPosts(posts: List<PostUiState>) {
    (adapter as PostsAdapter).submitList(posts)
}
