package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.ChildCareFacility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CHILD_CARE")
data class NetworkChildCareFacility(
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
    @SerialName("isSaturdayOperate")
    val isSaturdayOperate: Boolean,
) : NetworkFacility() {

    fun toChildCareFacility(
        reviewCount: Int,
        starPointAvg: Double,
    ) = ChildCareFacility(
        id = id,
        name = name,
        childCareService = service.asService(),
        point = point.asPoint(),
        address = address.asAddress(),
        detailUrl = detailUrl,
        isSaturdayOperate = isSaturdayOperate,
        reviewCount = reviewCount,
        starPointAvg = starPointAvg,
    )
}
