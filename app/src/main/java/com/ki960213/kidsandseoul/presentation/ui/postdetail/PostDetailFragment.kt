package com.ki960213.kidsandseoul.presentation.ui.postdetail

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentPostDetailBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.ui.MainViewModel
import com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog.AuthorMoreDialog
import com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog.DeleteCommentConfirmDialog
import com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog.DeletedPostFetchDialog
import com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog.NotAuthorMoreDialog
import com.ki960213.kidsandseoul.presentation.ui.postdetail.recyclerview.PostDetailItemsAdapter
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailItemUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment :
    BaseFragment<FragmentPostDetailBinding>(R.layout.fragment_post_detail) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val postDetailViewModel: PostDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupWindowInsetsListener(view)
        setupPostDetailItemRecyclerView()

        observeUiEvent()
    }

    private fun setupDataBinding() {
        binding.viewModel = postDetailViewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
        binding.onMoreButtonClick = ::showMoreDialog
        binding.onCommentSendButtonClick = ::handleCommentSendButtonClick
        binding.onLoginButtonClick = mainViewModel::dispatchLoginEvent
    }

    private fun showMoreDialog() {
        if (postDetailViewModel.loginUserIsAuthor) {
            AuthorMoreDialog().show(childFragmentManager, null)
        } else {
            NotAuthorMoreDialog().show(childFragmentManager, null)
        }
    }

    private fun handleCommentSendButtonClick(content: String) {
        postDetailViewModel.sendComment(content)
        binding.etPostDetailInputComment.clearFocus()
        binding.etPostDetailInputComment.text.clear()
    }

    private fun setupWindowInsetsListener(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val navigatorBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            if (imeVisible) {
                binding.root.setPadding(0, 0, 0, imeHeight - navigatorBarHeight)
            } else {
                binding.root.setPadding(0, 0, 0, 0)
            }
            insets
        }
    }

    private fun setupPostDetailItemRecyclerView() {
        binding.rvPostDetail.adapter = PostDetailItemsAdapter(
            onProfileUiClick = ::navigateToProfile,
            onLikeToggle = postDetailViewModel::setLike,
            onDeleteButtonClick = ::showDeleteCommentConfirmDialog,
            onChildCommentButtonClick = ::handleChildCommentButtonClick
        )
    }

    private fun navigateToProfile(userId: String) {
        findNavController().navigateSafely(
            PostDetailFragmentDirections.actionPostDetailFragmentToProfileFragment(userId)
        )
    }

    private fun showDeleteCommentConfirmDialog(commentId: String) {
        val dialog = DeleteCommentConfirmDialog()
        dialog.arguments = DeleteCommentConfirmDialog.createArgs(commentId)
        dialog.show(childFragmentManager, null)
    }

    private fun handleChildCommentButtonClick(commentId: String) {
        // TODO("대댓글 기능 구현 보류")
    }

    private fun observeUiEvent() {
        repeatOnStarted { postDetailViewModel.uiEvent.collect(::handleUiEvent) }
    }

    private fun handleUiEvent(uiEvent: PostDetailUiEvent) {
        when (uiEvent) {
            PostDetailUiEvent.DeletedPostFetch -> showDeleteFetchDialog()
        }
    }

    private fun showDeleteFetchDialog() {
        DeletedPostFetchDialog().show(childFragmentManager, null)
    }

    companion object {

        const val KEY_POST_ID = "postId"
    }
}

@BindingAdapter("app:postDetail_items")
fun RecyclerView.setPostDetailItems(uiStats: List<PostDetailItemUiState>) {
    (adapter as PostDetailItemsAdapter).submitList(uiStats)
}
