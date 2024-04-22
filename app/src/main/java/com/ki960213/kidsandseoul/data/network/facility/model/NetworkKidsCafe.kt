package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.KidsCafe
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate

@Serializable
@SerialName("KIDS_CAFE")
data class NetworkKidsCafe(
    @SerialName("id")
    override val id: Long,
    @SerialName("name")
    override val name: String,
    @SerialName("point")
    override val point: NetworkPoint,
    @SerialName("address")
    override val address: NetworkAddress,
    @SerialName("detailUrl")
    override val detailUrl: String,
    @SerialName("contact")
    val contact: String,
    @SerialName("operatingDays")
    val operatingDays: List<String>,    // DayOfWeek
    @SerialName("closedDays")
    val closeDays: List<String>,    // LocalDate
    @SerialName("appliableAges")
    val appliableAges: List<Int>,
) : NetworkFacility() {

    fun toKidsCafe(
        reviewCount: Int,
        starPointAvg: Double,
    ) = KidsCafe(
        id = id,
        name = name,
        point = point.asPoint(),
        address = address.asAddress(),
        detailUrl = detailUrl,
        contact = contact,
        operatingDays = operatingDays.asDayOfWeeks(),
        appliableAges = appliableAges[0]..appliableAges[1],
        closeDays = closeDays.map { LocalDate.parse(it) }.toSet(),
        reviewCount = reviewCount,
        starPointAvg = starPointAvg,
    )
}

fun List<String>.asDayOfWeeks(): List<DayOfWeek> = map { DayOfWeek.valueOf(it) }
