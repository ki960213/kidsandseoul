package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.ChildCareFacility
import com.ki960213.domain.facility.model.Facility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NetworkChildCareFacility")
data class NetworkChildCareFacility(
    override val id: Long,
    override val name: String,
    val childCareFacilityType: NetworkChildCareFacilityType,
    override val point: NetworkPoint,
    override val address: NetworkAddress,
    override val detailUrl: String,
    override val reviewCount: Int,
    override val starPointAvg: Double,
    val isSaturdayOperate: Boolean,
) : NetworkFacility {

    override fun asFacility(): Facility = ChildCareFacility(
        id = id,
        name = name,
        childCareFacilityType = childCareFacilityType.asChildCareFacilityType(),
        point = point.asPoint(),
        address = address.asAddress(),
        detailUrl = detailUrl,
        isSaturdayOperate = isSaturdayOperate,
        reviewCount = reviewCount,
        starPointAvg = starPointAvg,
    )
}
