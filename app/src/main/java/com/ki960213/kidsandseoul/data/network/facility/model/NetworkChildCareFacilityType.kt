package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.ChildCareFacilityType

enum class NetworkChildCareFacilityType {
    OUR_NEIGHBORHOOD_GROWING_CENTER,
    CO_PARENTING_ROOM,
    LOCAL_CHILDREN_CENTER,
    CO_PARENTING_SHARING_CENTER;

    fun asChildCareFacilityType(): ChildCareFacilityType = when (this) {
        OUR_NEIGHBORHOOD_GROWING_CENTER -> ChildCareFacilityType.OUR_NEIGHBORHOOD_GROWING_CENTER
        CO_PARENTING_ROOM -> ChildCareFacilityType.CO_PARENTING_ROOM
        LOCAL_CHILDREN_CENTER -> ChildCareFacilityType.LOCAL_CHILDREN_CENTER
        CO_PARENTING_SHARING_CENTER -> ChildCareFacilityType.CO_PARENTING_SHARING_CENTER
    }

    companion object {

        fun from(type: ChildCareFacilityType): NetworkChildCareFacilityType = when (type) {
            ChildCareFacilityType.OUR_NEIGHBORHOOD_GROWING_CENTER -> OUR_NEIGHBORHOOD_GROWING_CENTER
            ChildCareFacilityType.CO_PARENTING_ROOM -> CO_PARENTING_ROOM
            ChildCareFacilityType.LOCAL_CHILDREN_CENTER -> LOCAL_CHILDREN_CENTER
            ChildCareFacilityType.CO_PARENTING_SHARING_CENTER -> CO_PARENTING_SHARING_CENTER
        }
    }
}
