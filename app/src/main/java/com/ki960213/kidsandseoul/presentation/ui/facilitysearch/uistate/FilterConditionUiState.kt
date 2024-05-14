package com.ki960213.kidsandseoul.presentation.ui.facilitysearch.uistate

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.administrativedong.model.Borough
import com.ki960213.domain.facility.model.AllFacilityFilterConditions
import com.ki960213.domain.facility.model.ChildCareFacilityFilterConditions
import com.ki960213.domain.facility.model.FacilityFilterConditions
import com.ki960213.domain.facility.model.KidsCafeFilterConditions
import com.ki960213.domain.facility.model.OtherFacilityFilterConditions

data class FilterConditionsUiState(
    val keyword: String = "",
    val detailConditions: DetailFilterConditionsUiState? = null,
    val age: Int? = null,
    val boroughs: Set<Borough> = emptySet(),
    val administrativeDongs: Set<AdministrativeDong> = emptySet(),
) {

    fun asFacilityFilterConditions(): FacilityFilterConditions = when (detailConditions) {
        is ChildCareFilterConditionsUiState -> ChildCareFacilityFilterConditions(
            name = keyword,
            age = age,
            boroughIds = boroughs.map { it.id }.toSet(),
            administrativeDongIds = administrativeDongs.map { it.id }.toSet(),
            childCareFacilityType = detailConditions.type,
            mustSaturdayOperate = detailConditions.mustSaturdayOperate,
        )

        is KidsCafeConditionsUiState -> KidsCafeFilterConditions(
            name = keyword,
            age = age,
            boroughIds = boroughs.map { it.id }.toSet(),
            administrativeDongIds = administrativeDongs.map { it.id }.toSet(),
            daysOfWeek = detailConditions.daysOfWeek,
        )

        is OtherDetailFilterConditionsUiState -> OtherFacilityFilterConditions(
            name = keyword,
            age = age,
            boroughIds = boroughs.map { it.id }.toSet(),
            administrativeDongIds = administrativeDongs.map { it.id }.toSet(),
            otherFacilityType = detailConditions.type,
        )

        null -> AllFacilityFilterConditions(
            name = keyword,
            age = age,
            boroughIds = boroughs.map { it.id }.toSet(),
            administrativeDongIds = administrativeDongs.map { it.id }.toSet(),
        )
    }
}
