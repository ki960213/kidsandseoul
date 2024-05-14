package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Facility
import kotlinx.serialization.Serializable

@Serializable
sealed interface NetworkFacility {

    val id: Long

    val name: String

    val point: NetworkPoint

    val address: NetworkAddress

    val detailUrl: String

    val reviewCount: Int

    val starPointAvg: Double

    fun asFacility(): Facility
}
