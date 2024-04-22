package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import android.os.Bundle
import android.view.View
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.DialogAuthorMoreBinding
import com.ki960213.kidsandseoul.presentation.common.base.KasBottomSheetDialogFragment

class AuthorMoreDialog :
    KasBottomSheetDialogFragment<DialogAuthorMoreBinding>(R.layout.dialog_author_more) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.onDeleteButtonClick = ::handleDeleteButtonClick
    }

    private fun handleDeleteButtonClick() {
        dismiss()
        PostDeleteConfirmDialog().show(parentFragmentManager, null)
    }
}
