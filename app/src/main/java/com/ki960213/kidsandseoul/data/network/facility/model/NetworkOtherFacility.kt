package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.OtherFacility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NetworkOtherFacility")
data class NetworkOtherFacility(
    override val id: Long,
    override val name: String,
    val otherFacilityType: NetworkOtherFacilityType,
    override val point: NetworkPoint,
    override val address: NetworkAddress,
    override val detailUrl: String,
    override val reviewCount: Int,
    override val starPointAvg: Double,
) : NetworkFacility {

    override fun asFacility(): Facility = OtherFacility(
        id = id,
        name = name,
        point = point.asPoint(),
        address = address.asAddress(),
        detailUrl = detailUrl,
        type = otherFacilityType.asOtherFacilityType(),
        reviewCount = reviewCount,
        starPointAvg = starPointAvg,
    )
}
