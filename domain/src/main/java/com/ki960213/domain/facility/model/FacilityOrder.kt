package com.ki960213.domain.facility.model

sealed interface FacilityOrder {
    data object None : FacilityOrder
    data class ReviewCount(val isDescending: Boolean = true) : FacilityOrder
    data class StarPointAvg(val isDescending: Boolean = true) : FacilityOrder
}
