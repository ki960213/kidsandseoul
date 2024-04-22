package com.ki960213.kidsandseoul.data.network.administrativedong.model

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAdministrativeDong(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("borough")
    val borough: NetworkBorough,
) {

    fun asAdministrativeDong() = AdministrativeDong(
        id = id,
        name = name,
        borough = borough.asBorough(),
    )
}
