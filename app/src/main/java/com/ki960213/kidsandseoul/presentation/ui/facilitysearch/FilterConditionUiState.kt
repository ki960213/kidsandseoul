package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.facility.model.ChildCareFacilityFilterCondition
import com.ki960213.domain.facility.model.FacilityFilterCondition
import com.ki960213.domain.facility.model.KidsCafeFilterCondition
import com.ki960213.domain.facility.model.OtherFacilityFilterCondition

data class FilterConditionUiState(
    val keyword: String? = null,
    val detailCondition: DetailFilterConditionUiState? = null,
    val age: Int? = null,
    val region: AdministrativeDong? = null,
) {

    val isNotEmpty: Boolean =
        keyword?.isNotBlank() ?: false || detailCondition != null || age != null || region != null

    fun asFacilityFilterCondition(): FacilityFilterCondition = when (detailCondition) {
        is ChildCareFilterConditionUiState -> ChildCareFacilityFilterCondition(
            name = keyword,
            age = age,
            administrativeDong = region,
            childCareService = detailCondition.service,
            isSaturdayOperate = detailCondition.mustSaturdayOperate,
        )

        is KidsCafeConditionUiState -> KidsCafeFilterCondition(
            name = keyword,
            age = age,
            administrativeDong = region,
            daysOfWeek = detailCondition.daysOfWeek,
        )

        is OtherDetailFilterConditionUiState -> OtherFacilityFilterCondition(
            name = keyword,
            age = age,
            administrativeDong = region,
            facilityType = detailCondition.type,
        )

        null -> FacilityFilterCondition(
            name = keyword,
            age = age,
            administrativeDong = region,
        )
    }
}
