package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Point
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPoint(
    val x: Double,
    val y: Double,
) {

    constructor(point: Point) : this(
        x = point.x,
        y = point.y,
    )

    fun asPoint() = Point(
        x = x,
        y = y,
    )
}
