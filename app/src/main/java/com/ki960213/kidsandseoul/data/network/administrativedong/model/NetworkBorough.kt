package com.ki960213.kidsandseoul.data.network.administrativedong.model

import com.ki960213.domain.administrativedong.model.Borough
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkBorough(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
) {

    fun asBorough() = Borough(
        id = id,
        name = name,
    )
}
