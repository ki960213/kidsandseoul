package com.ki960213.kidsandseoul.presentation.common.uistate

enum class ServiceUiState(val isNotOtherFacility: Boolean) {
    OUR_NEIGHBORHOOD_GROWING_CENTER(true),
    CO_PARENTING_ROOM(true),
    LOCAL_CHILDREN_CENTER(true),
    CO_PARENTING_SHARING_CENTER(true),
    KIDS_CAFE(true),
    OUTDOOR(false),
    EXPERIENCE(false),
    MEDICAL(false),
    LIBRARY(false);

    val isOtherFacility: Boolean = !isNotOtherFacility
}
