package com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import com.ki960213.domain.facility.model.Facilities
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.Point
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilitiesMapBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import com.ki960213.kidsandseoul.presentation.ui.MainActivity
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.FirstScreenFragmentDirections
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates.observable

@AndroidEntryPoint
class FacilitiesMapFragment :
    BaseFragment<FragmentFacilitiesMapBinding>(R.layout.fragment_facilities_map) {

    private val viewModel: FacilitiesMapViewModel by viewModels()

    private var naverMap: NaverMap? by observable(null) { _, _, newValue ->
        newValue?.onMapClickListener = NaverMap.OnMapClickListener { _, _ ->
            viewModel.selectFacility(null)
        }
    }

    private var markers: List<Marker> by observable(emptyList()) { _, oldValue, newValue ->
        oldValue.forEach { it.map = null }
        newValue.forEach { it.map = naverMap }
    }

    private var selectedMarker: Pair<Long, Marker>? by observable(null) { _, oldValue, newValue ->
        oldValue?.second?.map = null
        newValue?.second?.map = naverMap
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupNaverMap()
        setupSearchInput()

        observeFacilities()
        observeSelectedFacility()
        observeUiEvent()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onFacilityInfoClick = ::navigateToFacilityDetail
        binding.onSearchButtonClick = { viewModel.setKeyword(it) }
        binding.onServiceClick = ::handleServiceClick
    }

    private fun navigateToFacilityDetail(facilityId: Long) {
        val direction = FirstScreenFragmentDirections
            .actionFirstScreenFragmentToFacilityDetailFragment(facilityId)
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun handleServiceClick(service: ServiceUiState) {
        if (naverMap == null) return
        viewModel.setService(service)
    }

    private fun setupNaverMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fcv_facilities_map) as MapFragment
        mapFragment.getMapAsync { naverMap = it }
    }

    private fun setupSearchInput() {
        binding.etFacilitiesMapNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setKeyword(binding.etFacilitiesMapNameInput.text.toString())
            }
            false
        }
    }

    private fun observeFacilities() {
        repeatOnStarted { viewModel.facilities.collect(::updateFacilities) }
    }

    private fun updateFacilities(facilities: Facilities) {
        markers = facilities.value.map { facility ->
            Marker().apply {
                position = facility.point.asLatLng()
                icon = OverlayImage.fromResource(R.drawable.ic_facilities_map_marker)
                setOnClickListener {
                    viewModel.selectFacility(facility.id)
                    true
                }
            }
        }
        if (selectedMarker?.first !in facilities.value.map { it.id }) {
            selectedMarker = null
            viewModel.selectFacility(null)
        }
    }

    private fun observeSelectedFacility() {
        repeatOnStarted { viewModel.selectedFacility.collect(::updateSelectedFacility) }
    }

    private fun updateSelectedFacility(facility: Facility?) {
        selectedMarker = if (facility == null) {
            null
        } else {
            facility.id to Marker().apply {
                position = facility.point.asLatLng()
                icon = OverlayImage.fromResource(R.drawable.ic_facilities_map_selected_marker)
                zIndex = 1
                isForceShowIcon = true
            }
        }
    }

    private fun Point.asLatLng() = LatLng(y, x)

    private fun observeUiEvent() {
        repeatOnStarted { viewModel.uiEvent.collect(::handleUiEvent) }
    }

    private fun handleUiEvent(uiEvent: FacilitiesMapUiEvent) {
        when (uiEvent) {
            FacilitiesMapUiEvent.KeywordTooShort -> requireContext().showToast(R.string.all_search_too_short_message)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetFilterCondition()
    }
}
