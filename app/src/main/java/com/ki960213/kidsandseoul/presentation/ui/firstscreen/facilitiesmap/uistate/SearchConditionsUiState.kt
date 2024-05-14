package com.ki960213.kidsandseoul.presentation.ui.firstscreen.facilitiesmap.uistate

import com.ki960213.domain.facility.model.AllFacilityFilterConditions
import com.ki960213.domain.facility.model.Area
import com.ki960213.domain.facility.model.ChildCareFacilityFilterConditions
import com.ki960213.domain.facility.model.ChildCareFacilityType
import com.ki960213.domain.facility.model.FacilityFilterConditions
import com.ki960213.domain.facility.model.KidsCafeFilterConditions
import com.ki960213.domain.facility.model.OtherFacilityFilterConditions
import com.ki960213.domain.facility.model.OtherFacilityType
import com.ki960213.domain.facility.model.Point
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import com.naver.maps.geometry.LatLngBounds

data class SearchConditionsUiState(
    val keyword: String = "",
    val service: ServiceUiState? = null,
    val latLngBounds: LatLngBounds? = null,
) {

    val isValid: Boolean = keyword.trim().length >= MIN_SEARCH_KEYWORD_LENGTH || service != null
    val isNotValidBecauseTooShortKeyword: Boolean = !isValid

    fun asFacilityFilterConditions(): FacilityFilterConditions = when (service) {
        ServiceUiState.OUR_NEIGHBORHOOD_GROWING_CENTER ->
            createFacilityFilterConditions(ChildCareFacilityType.OUR_NEIGHBORHOOD_GROWING_CENTER)

        ServiceUiState.CO_PARENTING_ROOM ->
            createFacilityFilterConditions(ChildCareFacilityType.CO_PARENTING_ROOM)

        ServiceUiState.LOCAL_CHILDREN_CENTER ->
            createFacilityFilterConditions(ChildCareFacilityType.LOCAL_CHILDREN_CENTER)

        ServiceUiState.CO_PARENTING_SHARING_CENTER ->
            createFacilityFilterConditions(ChildCareFacilityType.CO_PARENTING_SHARING_CENTER)

        ServiceUiState.KIDS_CAFE -> KidsCafeFilterConditions(
            name = keyword,
            area = latLngBounds?.asArea(),
        )

        ServiceUiState.OUTDOOR -> createFacilityFilterConditions(OtherFacilityType.OUTDOOR)
        ServiceUiState.EXPERIENCE -> createFacilityFilterConditions(OtherFacilityType.EXPERIENCE)
        ServiceUiState.MEDICAL -> createFacilityFilterConditions(OtherFacilityType.MEDICAL)
        ServiceUiState.LIBRARY -> createFacilityFilterConditions(OtherFacilityType.LIBRARY)
        null -> AllFacilityFilterConditions(name = keyword)
    }

    private fun createFacilityFilterConditions(
        childCareFacilityType: ChildCareFacilityType,
    ): ChildCareFacilityFilterConditions = ChildCareFacilityFilterConditions(
        name = keyword,
        childCareFacilityType = childCareFacilityType,
        area = latLngBounds?.asArea(),
    )

    private fun createFacilityFilterConditions(
        otherFacilityType: OtherFacilityType,
    ): OtherFacilityFilterConditions = OtherFacilityFilterConditions(
        name = keyword,
        otherFacilityType = otherFacilityType,
        area = latLngBounds?.asArea(),
    )

    private fun LatLngBounds.asArea() = Area(
        leftTop = Point(
            x = northWest.longitude,
            y = northWest.latitude,
        ),
        rightTop = Point(
            x = northEast.longitude,
            y = northEast.latitude,
        ),
        leftBottom = Point(
            x = southWest.longitude,
            y = southWest.latitude,
        ),
        rightBottom = Point(
            x = southEast.longitude,
            y = southEast.latitude,
        ),
    )

    companion object {

        private const val MIN_SEARCH_KEYWORD_LENGTH = 2
    }
}
