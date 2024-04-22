package com.ki960213.kidsandseoul.presentation.ui.join

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.domain.auth.model.Authentication
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentJoinBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongsAdapter
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinFragment : BaseFragment<FragmentJoinBinding>(R.layout.fragment_join) {

    private val viewModel: JoinViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupRecyclerViewAdapter()
        setupNameEditText()

        observeUiEvent()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
        binding.onJoinButtonClick = ::handleJoinButtonClick
        binding.onStartButtonClick = ::navigateUpToHome
    }

    private fun handleJoinButtonClick() {
        if (viewModel.isJoinWaiting.value) return
        viewModel.join()
    }

    private fun navigateUpToHome() {
        findNavController().popBackStack(R.id.firstScreenFragment, false)
    }

    private fun setupRecyclerViewAdapter() {
        binding.rvJoinBoroughs.adapter = BoroughsAdapter { viewModel.selectBorough(it) }
        binding.rvJoinAdministrativeDongs.adapter =
            AdministrativeDongsAdapter { viewModel.selectAdministrativeDong(it) }
    }

    private fun setupNameEditText() {
        binding.tietJoinName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) binding.tietJoinName.clearFocus()
            false
        }
    }

    private fun observeUiEvent() {
        repeatOnStarted {
            viewModel.uiEvent.collect(::handleUiEvent)
        }
    }

    private fun handleUiEvent(uiEvent: JoinUiEvent) {
        when (uiEvent) {
            JoinUiEvent.JoinFail -> {
                requireContext().showToast("회원가입에 실패했어요. 다시 시도해보세요!")
                findNavController().popBackStack()
            }
        }
    }

    companion object {

        const val KEY_AUTHENTICATION = "authentication"

        fun getArgs(authentication: Authentication) = Bundle().apply {
            putParcelable(KEY_AUTHENTICATION, authentication.asArgs())
        }
    }
}
