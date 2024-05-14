package com.ki960213.domain.facility.model

data class OtherFacility(
    override val id: Long,
    override val name: String,
    override val point: Point,
    override val address: Address,
    override val detailUrl: String,
    override val reviewCount: Int,
    override val starPointAvg: Double,
    val type: OtherFacilityType,
) : Facility
