package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Area
import kotlinx.serialization.Serializable

@Serializable
data class NetworkArea(
    val leftTop: NetworkPoint,
    val rightTop: NetworkPoint,
    val leftBottom: NetworkPoint,
    val rightBottom: NetworkPoint,
) {
    constructor(area: Area) : this(
        leftTop = NetworkPoint(area.leftTop),
        rightTop = NetworkPoint(area.rightTop),
        leftBottom = NetworkPoint(area.leftBottom),
        rightBottom = NetworkPoint(area.rightBottom),
    )
}
