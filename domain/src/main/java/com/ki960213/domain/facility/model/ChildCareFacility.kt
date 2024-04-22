package com.ki960213.domain.facility.model

data class ChildCareFacility(
    override val id: Long,
    override val name: String,
    override val point: Point,
    override val address: Address,
    override val detailUrl: String,
    override val reviewCount: Int,
    override val starPointAvg: Double,
    val childCareService: ChildCareService,
    val isSaturdayOperate: Boolean,
) : Facility
