package com.ki960213.kidsandseoul.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ki960213.kidsandseoul.presentation.common.extension.dp

abstract class KasBottomSheetDialogFragment<V : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
) : BottomSheetDialogFragment() {

    private var _binding: V? = null
    protected val binding get() = _binding!!

    private val bottomSheet: FrameLayout by lazy {
        val parentView = binding.root.parent as View
        parentView.findViewById(com.google.android.material.R.id.design_bottom_sheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheet.setupBackground()
        bottomSheet.setupBehavior()
    }

    private fun FrameLayout.setupBackground() {
        background = null
        setPadding(6.dp)
    }

    private fun FrameLayout.setupBehavior() {
        val behavior = BottomSheetBehavior.from(this)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
