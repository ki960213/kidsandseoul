package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.OtherFacility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OTHER")
data class NetworkOtherFacility(
    @SerialName("id")
    override val id: Long,
    @SerialName("name")
    override val name: String,
    @SerialName("service")
    val service: NetworkService,
    @SerialName("point")
    override val point: NetworkPoint,
    @SerialName("address")
    override val address: NetworkAddress,
    @SerialName("detailUrl")
    override val detailUrl: String,
) : NetworkFacility() {

    fun toOtherFacility(
        reviewCount: Int,
        starPointAvg: Double,
    ) = OtherFacility(
        id = id,
        name = name,
        point = point.asPoint(),
        address = address.asAddress(),
        detailUrl = detailUrl,
        type = service.asFacilityType(),
        reviewCount = reviewCount,
        starPointAvg = starPointAvg,
    )
}
