package com.ki960213.domain.facility.model

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import java.time.DayOfWeek

open class FacilityFilterCondition(
    open val name: String? = null,
    open val age: Int? = null,
    open val administrativeDong: AdministrativeDong? = null,
) {

    open fun isSatisfiedBy(facility: Facility): Boolean = true
}

data class ChildCareFacilityFilterCondition(
    override val name: String? = null,
    override val administrativeDong: AdministrativeDong? = null,
    override val age: Int? = null,
    val childCareService: ChildCareService? = null,
    val isSaturdayOperate: Boolean? = null,
) : FacilityFilterCondition(name, age, administrativeDong) {

    override fun isSatisfiedBy(facility: Facility): Boolean {
        if (facility !is ChildCareFacility) return false
        if (isSaturdayOperate == true) return facility.isSaturdayOperate
        return super.isSatisfiedBy(facility)
    }
}

data class KidsCafeFilterCondition(
    override val name: String? = null,
    override val administrativeDong: AdministrativeDong? = null,
    override val age: Int? = null,
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
) : FacilityFilterCondition(name, age, administrativeDong) {

    override fun isSatisfiedBy(facility: Facility): Boolean {
        if (facility !is KidsCafe) return false
        if (daysOfWeek.any { day -> day !in facility.operatingDays }) return false
        return super.isSatisfiedBy(facility)
    }
}

data class OtherFacilityFilterCondition(
    override val name: String? = null,
    override val administrativeDong: AdministrativeDong? = null,
    override val age: Int? = null,
    val facilityType: FacilityType? = null,
) : FacilityFilterCondition(name, age, administrativeDong) {

    override fun isSatisfiedBy(facility: Facility): Boolean = super.isSatisfiedBy(facility)
}
