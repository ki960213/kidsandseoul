package com.ki960213.kidsandseoul.data.network.facility.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAppliableAges(
    @SerialName("start")
    val start: Int,
    @SerialName("end")
    val end: Int,
) {

    fun asIntRange(): IntRange = start..end
}
