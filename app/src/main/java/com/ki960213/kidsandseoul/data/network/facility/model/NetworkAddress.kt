package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.Address
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAddress(
    @SerialName("zipCode")
    val zipCode: String,
    @SerialName("basicAddress")
    val baseAddress: String,
    @SerialName("detailAddress")
    val detail: String,
) {

    fun asAddress() = Address(
        zipCode = zipCode,
        baseAddress = baseAddress,
        detail = detail,
    )
}
