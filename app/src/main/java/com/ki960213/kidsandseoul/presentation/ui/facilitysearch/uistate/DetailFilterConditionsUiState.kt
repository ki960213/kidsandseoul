package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate

import com.ki960213.domain.facility.model.ChildCareFacilityType
import com.ki960213.domain.facility.model.OtherFacilityType
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import java.time.DayOfWeek

sealed interface DetailFilterConditionsUiState {

    val mustSaturdayOperate: Boolean
    val selectedDaysOfWeek: Set<DayOfWeek>

    fun isSelected(service: ChildCareFacilityType): Boolean
    fun isSelected(type: OtherFacilityType): Boolean

    companion object {
        fun from(serviceUiState: ServiceUiState): DetailFilterConditionsUiState = when (serviceUiState) {
            ServiceUiState.OUR_NEIGHBORHOOD_GROWING_CENTER -> ChildCareFilterConditionsUiState(
                type = ChildCareFacilityType.OUR_NEIGHBORHOOD_GROWING_CENTER,
            )

            ServiceUiState.CO_PARENTING_ROOM -> ChildCareFilterConditionsUiState(
                type = ChildCareFacilityType.CO_PARENTING_ROOM,
            )

            ServiceUiState.LOCAL_CHILDREN_CENTER -> ChildCareFilterConditionsUiState(
                type = ChildCareFacilityType.LOCAL_CHILDREN_CENTER,
            )

            ServiceUiState.CO_PARENTING_SHARING_CENTER -> ChildCareFilterConditionsUiState(
                type = ChildCareFacilityType.CO_PARENTING_SHARING_CENTER,
            )

            ServiceUiState.KIDS_CAFE -> KidsCafeConditionsUiState()
            ServiceUiState.OUTDOOR -> OtherDetailFilterConditionsUiState(
                type = OtherFacilityType.OUTDOOR,
            )

            ServiceUiState.EXPERIENCE -> OtherDetailFilterConditionsUiState(
                type = OtherFacilityType.EXPERIENCE
            )

            ServiceUiState.MEDICAL -> OtherDetailFilterConditionsUiState(
                type = OtherFacilityType.MEDICAL
            )

            ServiceUiState.LIBRARY -> OtherDetailFilterConditionsUiState(
                type = OtherFacilityType.LIBRARY
            )
        }
    }
}

data class ChildCareFilterConditionsUiState(
    val type: ChildCareFacilityType? = null,
    override val mustSaturdayOperate: Boolean = false,
) : DetailFilterConditionsUiState {

    override val selectedDaysOfWeek: Set<DayOfWeek> = emptySet()

    override fun isSelected(service: ChildCareFacilityType): Boolean = this.type == service

    override fun isSelected(type: OtherFacilityType): Boolean = false
}

data class KidsCafeConditionsUiState(
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
) : DetailFilterConditionsUiState {

    override val mustSaturdayOperate: Boolean = false
    override val selectedDaysOfWeek: Set<DayOfWeek> = daysOfWeek

    override fun isSelected(service: ChildCareFacilityType): Boolean = false

    override fun isSelected(type: OtherFacilityType): Boolean = false
}

data class OtherDetailFilterConditionsUiState(
    val type: OtherFacilityType? = null,
) : DetailFilterConditionsUiState {

    override val mustSaturdayOperate: Boolean = false
    override val selectedDaysOfWeek: Set<DayOfWeek> = emptySet()

    override fun isSelected(service: ChildCareFacilityType): Boolean = false

    override fun isSelected(type: OtherFacilityType): Boolean = this.type == type
}
