package com.ki960213.kidsandseoul.presentation.ui.postdetail.dialog

import android.os.Bundle
import android.view.View
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.DialogNotAuthorMoreBinding
import com.ki960213.kidsandseoul.presentation.common.base.KasBottomSheetDialogFragment

class NotAuthorMoreDialog : KasBottomSheetDialogFragment<DialogNotAuthorMoreBinding>(R.layout.dialog_not_author_more) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.onReportButtonClick = ::handleReportButtonClick
    }

    private fun handleReportButtonClick() {
        dismiss()
        showReportConfirmDialog()
    }

    private fun showReportConfirmDialog() {
        PostReportConfirmDialog().show(parentFragmentManager, null)
    }
}
