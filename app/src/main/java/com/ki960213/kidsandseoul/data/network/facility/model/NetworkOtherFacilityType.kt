package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.OtherFacilityType

enum class NetworkOtherFacilityType {
    OUTDOOR, EXPERIENCE, MEDICAL, LIBRARY;

    fun asOtherFacilityType(): OtherFacilityType = when (this) {
        OUTDOOR -> OtherFacilityType.OUTDOOR
        EXPERIENCE -> OtherFacilityType.EXPERIENCE
        MEDICAL -> OtherFacilityType.MEDICAL
        LIBRARY -> OtherFacilityType.LIBRARY
    }

    companion object {

        fun from(type: OtherFacilityType): NetworkOtherFacilityType = when (type) {
            OtherFacilityType.OUTDOOR -> OUTDOOR
            OtherFacilityType.EXPERIENCE -> EXPERIENCE
            OtherFacilityType.MEDICAL -> MEDICAL
            OtherFacilityType.LIBRARY -> LIBRARY
        }
    }
}
