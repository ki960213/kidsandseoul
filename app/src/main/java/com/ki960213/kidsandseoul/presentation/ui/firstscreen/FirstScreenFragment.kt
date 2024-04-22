package com.ki960213.kidsandseoul.presentation.ui.firstscreen

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFirstScreenBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstScreenFragment :
    BaseFragment<FragmentFirstScreenBinding>(R.layout.fragment_first_screen) {

    private val childNavController by lazy {
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.fcv_home) as NavHostFragment
        navHostFragment.findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNavBar()
    }

    private fun setupBottomNavBar() {
        binding.bnvHome.setupWithNavController(childNavController)
    }
}
