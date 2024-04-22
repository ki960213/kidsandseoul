package com.ki960213.kidsandseoul.presentation.common.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ki960213.kidsandseoul.databinding.DialogConfirmBinding
import com.ki960213.kidsandseoul.presentation.common.extension.dp

abstract class ConfirmDialogFragment : DialogFragment() {

    abstract val title: String
    abstract val message: String
    open val positiveButtonLabel: String = "확인"
    open val negativeButtonLabel: String = "취소"

    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupWindow()
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.title = title
        binding.message = message
        binding.positiveButtonLabel = positiveButtonLabel
        binding.negativeButtonLabel = negativeButtonLabel
        binding.onPositiveButtonClick = ::onPositiveButtonClick
        binding.onNegativeButtonClick = ::onNegativeButtonClick
    }

    private fun onPositiveButtonClick() {
        action()
        dismiss()
    }

    abstract fun action()

    private fun onNegativeButtonClick() {
        dismiss()
    }

    private fun setupWindow() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.let {
            it.width = 280.dp
            it.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }
}
