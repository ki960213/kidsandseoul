package com.ki960213.kidsandseoul.presentation.ui.notifications

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentNotificationsBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment :
    BaseFragment<FragmentNotificationsBinding>(R.layout.fragment_notifications) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.onBackButtonClick = { findNavController().popBackStack() }
    }
}
