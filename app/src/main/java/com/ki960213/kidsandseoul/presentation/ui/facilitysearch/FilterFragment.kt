package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

import android.os.Bundle
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.kid.model.Kid
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFilterBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.ui.MainViewModel
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.kids.KidsAdapter
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongsAdapter
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(R.layout.fragment_filter) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val facilitySearchViewModel: FacilitySearchViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupRecyclerViews()
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = facilitySearchViewModel
        binding.onCloseButtonClick = { parentFragmentManager.popBackStack() }
        binding.onKidAddButtonClick = ::handleKidAddButtonClick
        binding.onApplyButtonClick = ::handleApplyButtonClick
    }

    private fun handleKidAddButtonClick() {
        val loginUserId = facilitySearchViewModel.loginUserId.value
        if (loginUserId == null) {
            mainViewModel.dispatchLoginEvent()
        } else {
            val direction = FacilitySearchFragmentDirections
                .actionFacilitySearchFragmentToKidAddFragment(loginUserId)
            requireParentFragment().findNavController().navigateSafely(direction)
        }
    }

    private fun handleApplyButtonClick() {
        facilitySearchViewModel.applyFilterCondition()
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun setupRecyclerViews() {
        binding.rvFilterKids.adapter = KidsAdapter(facilitySearchViewModel::changeConditionToFitKid)
        binding.rvFilterBoroughs.adapter = BoroughsAdapter(facilitySearchViewModel::selectBorough)
        binding.rvFilterAdministrativeDongs.adapter =
            AdministrativeDongsAdapter(facilitySearchViewModel::selectAdministrativeDong)
    }
}

@BindingAdapter("app:filter_kids")
fun RecyclerView.setKids(kids: List<Kid>) {
    (adapter as KidsAdapter).submitList(kids)
}
