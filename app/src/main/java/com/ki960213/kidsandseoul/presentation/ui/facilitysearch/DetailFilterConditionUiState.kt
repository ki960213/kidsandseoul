package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

import com.ki960213.domain.facility.model.ChildCareService
import com.ki960213.domain.facility.model.FacilityType
import java.time.DayOfWeek

sealed interface DetailFilterConditionUiState {

    val isComplete: Boolean
    val isOurNeighborGrowingCenterSelected: Boolean
    val isLocalChildrenCenterSelected: Boolean
    val isCoParentingRoomSelected: Boolean
    val isCoParentingSharingCenterSelected: Boolean
    val isKidsCafeSelected: Boolean
    val isOutdoorSelected: Boolean
    val isExperienceSelected: Boolean
    val isMedicalSelected: Boolean
    val isLibrarySelected: Boolean
    val mustSaturdayOperate: Boolean
    val selectedDaysOfWeek: Set<DayOfWeek>
}

data class ChildCareFilterConditionUiState(
    val service: ChildCareService? = null,
    override val mustSaturdayOperate: Boolean = false,
) : DetailFilterConditionUiState {

    override val isComplete: Boolean = service != null
    override val isOurNeighborGrowingCenterSelected: Boolean =
        service == ChildCareService.OUR_NEIGHBORHOOD_GROWING_CENTER
    override val isLocalChildrenCenterSelected: Boolean =
        service == ChildCareService.LOCAL_CHILDREN_CENTER
    override val isCoParentingRoomSelected: Boolean = service == ChildCareService.CO_PARENTING_ROOM
    override val isCoParentingSharingCenterSelected: Boolean =
        service == ChildCareService.CO_PARENTING_SHARING_CENTER
    override val isKidsCafeSelected: Boolean = false
    override val isOutdoorSelected: Boolean = false
    override val isExperienceSelected: Boolean = false
    override val isMedicalSelected: Boolean = false
    override val isLibrarySelected: Boolean = false
    override val selectedDaysOfWeek: Set<DayOfWeek> = emptySet()
}

data class KidsCafeConditionUiState(
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
) : DetailFilterConditionUiState {

    override val isComplete: Boolean = true
    override val isOurNeighborGrowingCenterSelected: Boolean = false
    override val isLocalChildrenCenterSelected: Boolean = false
    override val isCoParentingRoomSelected: Boolean = false
    override val isCoParentingSharingCenterSelected: Boolean = false
    override val isKidsCafeSelected: Boolean = true
    override val isOutdoorSelected: Boolean = false
    override val isExperienceSelected: Boolean = false
    override val isMedicalSelected: Boolean = false
    override val isLibrarySelected: Boolean = false
    override val mustSaturdayOperate: Boolean = false
    override val selectedDaysOfWeek: Set<DayOfWeek> = daysOfWeek
}

data class OtherDetailFilterConditionUiState(
    val type: FacilityType? = null,
) : DetailFilterConditionUiState {

    override val isComplete: Boolean = type != null
    override val isOurNeighborGrowingCenterSelected: Boolean = false
    override val isLocalChildrenCenterSelected: Boolean = false
    override val isCoParentingRoomSelected: Boolean = false
    override val isCoParentingSharingCenterSelected: Boolean = false
    override val isKidsCafeSelected: Boolean = false
    override val isOutdoorSelected: Boolean = type == FacilityType.OUTDOOR
    override val isExperienceSelected: Boolean = type == FacilityType.EXPERIENCE
    override val isMedicalSelected: Boolean = type == FacilityType.MEDICAL
    override val isLibrarySelected: Boolean = type == FacilityType.LIBRARY
    override val mustSaturdayOperate: Boolean = false
    override val selectedDaysOfWeek: Set<DayOfWeek> = emptySet()
}
