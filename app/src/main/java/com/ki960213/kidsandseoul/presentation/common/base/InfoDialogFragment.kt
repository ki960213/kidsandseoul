package com.ki960213.kidsandseoul.presentation.common.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ki960213.kidsandseoul.databinding.DialogInfoBinding
import com.ki960213.kidsandseoul.presentation.common.extension.dp

abstract class InfoDialogFragment : DialogFragment() {

    abstract val title: String
    abstract val message: String

    private var _binding: DialogInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupWindow()
        isCancelable = false
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.title = title
        binding.message = message
        binding.onButtonClick = ::handleButtonClick
    }

    private fun handleButtonClick() {
        action()
        dismiss()
    }

    /**
     * 버튼을 눌렀을 때 실행할 함수
     */
    open fun action() {}

    private fun setupWindow() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.let {
            it.width = 280.dp
            it.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }
}
