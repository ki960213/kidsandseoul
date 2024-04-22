package com.ki960213.kidsandseoul.presentation.ui.kidadd

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentKidAddBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongsAdapter
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KidAddFragment : BaseFragment<FragmentKidAddBinding>(R.layout.fragment_kid_add) {

    private val viewModel: KidAddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupRecyclerViewAdapter()
        setupKidNameEditText()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackClick = { findNavController().popBackStack() }
        binding.onBirthDateButtonClick = ::showDatePickerDialog
        binding.onCancelButtonClick = { findNavController().popBackStack() }
        binding.onSaveButtonClick = ::handleSaveButtonClick
    }

    private fun showDatePickerDialog() {
        DatePickerFragment().show(childFragmentManager, "DatePicker")
    }

    private fun handleSaveButtonClick() {
        viewModel.addKid()
        findNavController().popBackStack()
    }

    private fun setupRecyclerViewAdapter() {
        binding.rvKidAddBoroughs.adapter = BoroughsAdapter { viewModel.selectBorough(it) }
        binding.rvKidAddAdministrativeDongs.adapter =
            AdministrativeDongsAdapter { viewModel.selectAdministrativeDong(it) }
    }

    private fun setupKidNameEditText() {
        binding.etKidAddNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) binding.etKidAddNameInput.clearFocus()
            false
        }
    }

    companion object {

        const val KEY_PARENT_ID = "parentId"
    }
}
