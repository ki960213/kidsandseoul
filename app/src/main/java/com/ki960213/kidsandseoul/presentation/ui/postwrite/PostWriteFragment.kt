package com.ki960213.kidsandseoul.presentation.ui.postwrite

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentPostWriteBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.FirstScreenFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostWriteFragment :
    BaseFragment<FragmentPostWriteBinding>(R.layout.fragment_post_write) {

    private val viewModel: PostWriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupBackPressedDispatcher()
        setupWindowInsetsListener(view)

        observeLoginUserId()
        observeUiEvent()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.onRegisterButtonClick = ::handleRegisterButtonClick
    }

    private fun handleRegisterButtonClick() {
        viewModel.register()
    }

    private fun setupBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (viewModel.isChanged) showExitConfirmDialog() else findNavController().popBackStack()
        }
    }

    private fun showExitConfirmDialog() {
        PostWriteExitConfirmDialog().show(childFragmentManager, null)
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

    private fun observeLoginUserId() {
        repeatOnStarted { viewModel.loginUserId.collect {} }
    }

    private fun observeUiEvent() {
        repeatOnStarted { viewModel.uiEvent.collect(::handleUiEvent) }
    }

    private fun handleUiEvent(uiEvent: PostWriteUiEvent) {
        when (uiEvent) {
            is PostWriteUiEvent.RegisterSuccess -> handleRegisterSuccess(uiEvent.postId)
        }
    }

    private fun handleRegisterSuccess(postId: String) {
        findNavController().popBackStack()
        val direction =
            FirstScreenFragmentDirections.actionFirstScreenFragmentToPostDetailFragment(postId)
        findNavController().navigateSafely(direction)
    }
}
