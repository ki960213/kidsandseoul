package com.ki960213.kidsandseoul.presentation.ui.reviewwrite

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentReviewWriteBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewWriteFragment :
    BaseFragment<FragmentReviewWriteBinding>(R.layout.fragment_review_write) {

    private val viewModel: ReviewWriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupWindowInsetsListener(view)
        setupBackPressedDispatcher()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.onRegisterButtonClick = ::handleRegisterButtonClick
    }

    private fun handleRegisterButtonClick() {
        viewModel.register()
        findNavController().popBackStack()
    }

    private fun setupBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (viewModel.isChanged) showExitConfirmDialog() else findNavController().popBackStack()
        }
    }

    private fun showExitConfirmDialog() {
        ReviewWriteExitConfirmDialog().show(childFragmentManager, null)
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

    companion object {

        const val KEY_FACILITY_ID = "facilityId"
    }
}
