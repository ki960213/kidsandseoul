package com.ki960213.domain.facility.model

import java.time.DayOfWeek

sealed interface FacilityFilterConditions {

    val ids: Set<Long>
    val name: String?
    val age: Int?
    val boroughIds: Set<Long>
    val administrativeDongIds: Set<Long>
    val area: Area?
}

data class ChildCareFacilityFilterConditions(
    override val ids: Set<Long> = emptySet(),
    override val name: String? = null,
    override val age: Int? = null,
    override val boroughIds: Set<Long> = emptySet(),
    override val administrativeDongIds: Set<Long> = emptySet(),
    override val area: Area? = null,
    val childCareFacilityType: ChildCareFacilityType? = null,
    val mustSaturdayOperate: Boolean = false,
) : FacilityFilterConditions

data class KidsCafeFilterConditions(
    override val ids: Set<Long> = emptySet(),
    override val name: String? = null,
    override val age: Int? = null,
    override val boroughIds: Set<Long> = emptySet(),
    override val administrativeDongIds: Set<Long> = emptySet(),
    override val area: Area? = null,
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
) : FacilityFilterConditions

data class OtherFacilityFilterConditions(
    override val ids: Set<Long> = emptySet(),
    override val name: String? = null,
    override val age: Int? = null,
    override val boroughIds: Set<Long> = emptySet(),
    override val administrativeDongIds: Set<Long> = emptySet(),
    override val area: Area? = null,
    val otherFacilityType: OtherFacilityType? = null,
) : FacilityFilterConditions

data class AllFacilityFilterConditions(
    override val ids: Set<Long> = emptySet(),
    override val name: String? = null,
    override val age: Int? = null,
    override val boroughIds: Set<Long> = emptySet(),
    override val administrativeDongIds: Set<Long> = emptySet(),
    override val area: Area? = null,
) : FacilityFilterConditions
