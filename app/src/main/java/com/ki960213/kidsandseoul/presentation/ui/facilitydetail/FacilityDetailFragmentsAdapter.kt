package com.ki960213.kidsandseoul.presentation.ui.facilitydetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.detailinfo.FacilityDetailInfoFragment
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.FacilityReviewsFragment

class FacilityDetailFragmentsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> FacilityDetailInfoFragment()
        else -> FacilityReviewsFragment()
    }
}
