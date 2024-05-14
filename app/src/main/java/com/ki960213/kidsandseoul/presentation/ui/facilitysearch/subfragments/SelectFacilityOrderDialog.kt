package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.subfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.DialogSelectFacilityOrderBinding
import com.ki960213.kidsandseoul.presentation.common.base.KasBottomSheetDialogFragment
import com.ki960213.kidsandseoul.presentation.ui.facilitysearch.FacilitySearchViewModel

class SelectFacilityOrderDialog : KasBottomSheetDialogFragment<DialogSelectFacilityOrderBinding>(R.layout.dialog_select_facility_order) {

    private val viewModel: FacilitySearchViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }
}
