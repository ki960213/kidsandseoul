package com.ki960213.domain.facility.model

import java.time.DayOfWeek
import java.time.LocalDate

data class KidsCafe(
    override val id: Long,
    override val name: String,
    override val point: Point,
    override val address: Address,
    override val detailUrl: String,
    override val reviewCount: Int,
    override val starPointAvg: Double,
    val contact: String,
    val operatingDays: List<DayOfWeek>,
    val closeDays: Set<LocalDate>,
    val appliableAges: IntRange,
) : Facility
