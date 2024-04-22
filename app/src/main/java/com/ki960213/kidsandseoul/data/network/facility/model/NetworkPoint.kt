package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Point
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPoint(
    @SerialName("x")
    val x: String,
    @SerialName("y")
    val y: String,
) {

    fun asPoint() = Point(
        x = x.toDouble(),
        y = y.toDouble(),
    )
}
