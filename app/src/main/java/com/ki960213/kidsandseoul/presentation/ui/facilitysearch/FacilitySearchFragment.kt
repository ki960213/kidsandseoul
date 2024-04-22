package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilitySearchBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FacilitySearchFragment :
    BaseFragment<FragmentFacilitySearchBinding>(R.layout.fragment_facility_search) {

    private val viewModel: FacilitySearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.serviceArg.value?.isOtherFacility == true) {
            lifecycleScope.launch {
                delay(500L)
                requireContext().showToast("세부 조건을 선택해주세요!")
            }
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<FilterFragment>(R.id.fcv_facility_search)
                addToBackStack(null)
            }
        }
    }

    companion object {

        const val KEY_FACILITY_SEARCH_ARG = "facilitySearchArg"
    }
}
