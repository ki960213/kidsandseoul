package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Address
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAddress(
    val zipCode: String,
    val base: String,
    val detail: String,
) {

    fun asAddress() = Address(
        zipCode = zipCode,
        baseAddress = base,
        detail = detail,
    )
}
