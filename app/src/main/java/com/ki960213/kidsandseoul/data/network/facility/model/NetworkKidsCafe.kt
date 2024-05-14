package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.KidsCafe
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
@SerialName("NetworkKidsCafe")
data class NetworkKidsCafe(
    override val id: Long,
    override val name: String,
    override val point: NetworkPoint,
    override val address: NetworkAddress,
    override val detailUrl: String,
    override val reviewCount: Int,
    override val starPointAvg: Double,
    val contact: String,
    val operatingDays: List<String>,    // DayOfWeek
    val appliableAges: List<Int>,
) : NetworkFacility {

    override fun asFacility(): Facility = KidsCafe(
        id = id,
        name = name,
        point = point.asPoint(),
        address = address.asAddress(),
        detailUrl = detailUrl,
        contact = contact,
        operatingDays = operatingDays.asDayOfWeeks(),
        appliableAges = appliableAges[0]..appliableAges[1],
        reviewCount = reviewCount,
        starPointAvg = starPointAvg,
    )
}

fun List<String>.asDayOfWeeks(): List<DayOfWeek> = map { DayOfWeek.valueOf(it) }
