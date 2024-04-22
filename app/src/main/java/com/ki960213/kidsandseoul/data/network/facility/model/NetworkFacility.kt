package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Facility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class NetworkFacility {

    @SerialName("id")
    abstract val id: Long

    @SerialName("name")
    abstract val name: String

    @SerialName("point")
    abstract val point: NetworkPoint

    @SerialName("address")
    abstract val address: NetworkAddress

    @SerialName("detailUrl")
    abstract val detailUrl: String
}

fun NetworkFacility.toFacility(reviewCount: Int, starPointAvg: Double): Facility =
    when (this) {
        is NetworkChildCareFacility -> toChildCareFacility(
            reviewCount = reviewCount,
            starPointAvg = starPointAvg
        )

        is NetworkKidsCafe -> toKidsCafe(
            reviewCount = reviewCount,
            starPointAvg = starPointAvg,
        )

        is NetworkOtherFacility -> toOtherFacility(
            reviewCount = reviewCount,
            starPointAvg = starPointAvg,
        )
    }
