package com.ki960213.domain.facility.model

sealed interface Facility {

    val id: Long
    val name: String
    val point: Point
    val address: Address
    val detailUrl: String
    val starPointAvg: Double
    val reviewCount: Int
}
