package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilitySearchBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FacilitySearchFragment :
    BaseFragment<FragmentFacilitySearchBinding>(R.layout.fragment_facility_search) {

    companion object {

        const val KEY_FACILITY_SEARCH_ARG = "facilitySearchArg"
    }
}
