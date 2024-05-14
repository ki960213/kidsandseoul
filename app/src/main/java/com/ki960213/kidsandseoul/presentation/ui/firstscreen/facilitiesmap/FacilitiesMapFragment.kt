package com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.Point
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentFacilitiesMapBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.hideKeyboard
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.common.extension.showToast
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import com.ki960213.kidsandseoul.presentation.ui.MainActivity
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.FirstScreenFragmentDirections
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap.uistate.FacilitiesMapUiEvent
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationSource
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates.observable

@AndroidEntryPoint
class FacilitiesMapFragment : BaseFragment<FragmentFacilitiesMapBinding>(R.layout.fragment_facilities_map) {

    private val viewModel: FacilitiesMapViewModel by viewModels()

    private val locationSource: LocationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private var naverMap: NaverMap? by observable(null) { _, _, newValue ->
        newValue?.locationSource = locationSource
        newValue?.onMapClickListener = NaverMap.OnMapClickListener { _, _ ->
            viewModel.selectFacility(null)
        }
        newValue?.addOnCameraChangeListener { _, _ ->
            viewModel.latLngBounds.value = newValue.contentBounds
        }
        markers.forEach { it.map = newValue }
        selectedMarker?.second?.map = newValue
    }

    private var markers: List<Marker> by observable(emptyList()) { _, oldValue, newValue ->
        oldValue.forEach { it.map = null }
        newValue.forEach { it.map = naverMap }
    }

    private var selectedMarker: Pair<Long, Marker>? by observable(null) { _, oldValue, newValue ->
        oldValue?.second?.map = null
        newValue?.second?.map = naverMap
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when {
                viewModel.selectedFacility.value != null -> viewModel.selectFacility(null)
                viewModel.canResetFilterConditions.value -> {
                    viewModel.resetFilterConditions()
                    isEnabled = false
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupNaverMap()
        setupSearchInput()
        setupBackPressedCallback()

        observeFacilities()
        observeSelectedFacility()
        observeUiEvent()
        observeCanResetFilterConditions()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onFacilityInfoClick = ::navigateToFacilityDetail
        binding.onSearchButtonClick = ::handleSearch
        binding.onServiceClick = ::handleServiceClick
    }

    private fun navigateToFacilityDetail(facilityId: Long) {
        val direction = FirstScreenFragmentDirections
            .actionFirstScreenFragmentToFacilityDetailFragment(facilityId)
        (requireActivity() as MainActivity).navController.navigateSafely(direction)
    }

    private fun handleServiceClick(service: ServiceUiState) {
        viewModel.service.value = service
        handleSearch()
    }

    private fun setupNaverMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fcv_facilities_map) as MapFragment
        mapFragment.getMapAsync { naverMap = it }
    }

    private fun setupSearchInput() {
        binding.etFacilitiesMapNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) handleSearch()
            false
        }
    }

    private fun handleSearch() {
        adjustZoomLevel()
        binding.etFacilitiesMapNameInput.clearFocus()
        hideKeyboard()
        viewModel.search()
    }

    private fun adjustZoomLevel() {
        val naverMap = naverMap ?: return
        when {
            viewModel.keyword.value.isNotBlank() -> naverMap.moveCameraToSeeAllOfSeoul()
            naverMap.cameraPosition.zoom < MAX_ZOOM_LEVEL_WITHOUT_KEYWORD -> {
                naverMap.cameraPosition = CameraPosition(
                    naverMap.cameraPosition.target,
                    MAX_ZOOM_LEVEL_WITHOUT_KEYWORD,
                )
            }
        }
        viewModel.latLngBounds.value = naverMap.contentBounds
    }

    private fun NaverMap.moveCameraToSeeAllOfSeoul() {
        cameraPosition = CameraPosition(LAT_LNG_MIDDLE_OF_SEOUL, ZOOM_LEVEL_VISIBLE_ALL_OF_SEOUL)
    }

    private fun setupBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun observeFacilities() {
        repeatOnStarted { viewModel.facilities.collect(::showFacilitiesWithMarker) }
    }

    private fun showFacilitiesWithMarker(facilities: List<Facility>) {
        markers = facilities.map { facility ->
            Marker().apply {
                position = facility.point.asLatLng()
                icon = OverlayImage.fromResource(R.drawable.ic_facilities_map_marker)
                setOnClickListener {
                    viewModel.selectFacility(facility.id)
                    true
                }
            }
        }
        if (selectedMarker?.first !in facilities.map { it.id }) {
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
                zIndex = ABOVE_ALL_Z_INDEX
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
            FacilitiesMapUiEvent.NotFoundFacilities -> requireContext().showToast(R.string.all_no_result_message)
        }
    }

    private fun observeCanResetFilterConditions() {
        repeatOnStarted {
            viewModel.canResetFilterConditions.collect { onBackPressedCallback.isEnabled = it }
        }
    }

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val ABOVE_ALL_Z_INDEX = 1
        private val LAT_LNG_MIDDLE_OF_SEOUL = LatLng(37.56661020000001, 126.97838809999945)
        private const val ZOOM_LEVEL_VISIBLE_ALL_OF_SEOUL = 9.0
        private const val MAX_ZOOM_LEVEL_WITHOUT_KEYWORD = 13.0
    }
}
