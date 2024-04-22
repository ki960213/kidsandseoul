package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.ChildCareService
import com.ki960213.domain.facility.model.FacilityType
import kotlinx.serialization.Serializable

@Serializable
enum class NetworkService {

    OUR_NEIGHBORHOOD_GROWING_CENTER,
    CO_PARENTING_ROOM,
    LOCAL_CHILDREN_CENTER,
    CO_PARENTING_SHARING_CENTER,
    YOUTH_AFTER_SCHOOL_ACADEMY,
    KIDS_CAFE,
    OUTDOOR,
    EXPERIENCE,
    MEDICAL,
    LIBRARY;

    fun asService(): ChildCareService = when (this) {
        OUR_NEIGHBORHOOD_GROWING_CENTER -> ChildCareService.OUR_NEIGHBORHOOD_GROWING_CENTER
        CO_PARENTING_ROOM -> ChildCareService.CO_PARENTING_ROOM
        LOCAL_CHILDREN_CENTER -> ChildCareService.LOCAL_CHILDREN_CENTER
        CO_PARENTING_SHARING_CENTER -> ChildCareService.CO_PARENTING_SHARING_CENTER
        YOUTH_AFTER_SCHOOL_ACADEMY -> ChildCareService.YOUTH_AFTER_SCHOOL_ACADEMY
        KIDS_CAFE -> throw AssertionError("KIDS_CATE는 Service 클래스로 변환할 수 없음")
        OUTDOOR -> throw AssertionError("OUTDOOR는 Service 클래스로 변환할 수 없음")
        EXPERIENCE -> throw AssertionError("EXPERIENCE는 Service 클래스로 변환할 수 없음")
        MEDICAL -> throw AssertionError("MEDICAL은 Service 클래스로 변환할 수 없음")
        LIBRARY -> throw AssertionError("LIBRARY는 Service 클래스로 변환할 수 없음")
    }

    fun asFacilityType(): FacilityType = when (this) {
        OUR_NEIGHBORHOOD_GROWING_CENTER -> throw AssertionError("OUR_NEIGHBORHOOD_GROWING_CENTER는 FacilityType 클래스로 변환할 수 없음")
        CO_PARENTING_ROOM -> throw AssertionError("CO_PARENTING_ROOM는 FacilityType 클래스로 변환할 수 없음")
        LOCAL_CHILDREN_CENTER -> throw AssertionError("LOCAL_CHILDREN_CENTER는 FacilityType 클래스로 변환할 수 없음")
        CO_PARENTING_SHARING_CENTER -> throw AssertionError("CO_PARENTING_SHARING_CENTER는 FacilityType 클래스로 변환할 수 없음")
        YOUTH_AFTER_SCHOOL_ACADEMY -> throw AssertionError("YOUTH_AFTER_SCHOOL_ACADEMY는 FacilityType 클래스로 변환할 수 없음")
        KIDS_CAFE -> throw AssertionError("KIDS_CAFE는 FacilityType 클래스로 변환할 수 없음")
        OUTDOOR -> FacilityType.OUTDOOR
        EXPERIENCE -> FacilityType.EXPERIENCE
        MEDICAL -> FacilityType.MEDICAL
        LIBRARY -> FacilityType.LIBRARY
    }
}

fun ChildCareService.asNetworkService(): NetworkService = when (this) {
    ChildCareService.OUR_NEIGHBORHOOD_GROWING_CENTER -> NetworkService.OUR_NEIGHBORHOOD_GROWING_CENTER
    ChildCareService.CO_PARENTING_ROOM -> NetworkService.CO_PARENTING_ROOM
    ChildCareService.LOCAL_CHILDREN_CENTER -> NetworkService.LOCAL_CHILDREN_CENTER
    ChildCareService.CO_PARENTING_SHARING_CENTER -> NetworkService.CO_PARENTING_SHARING_CENTER
    ChildCareService.YOUTH_AFTER_SCHOOL_ACADEMY -> NetworkService.YOUTH_AFTER_SCHOOL_ACADEMY
}

fun FacilityType.asNetworkService(): NetworkService = when (this) {
    FacilityType.OUTDOOR -> NetworkService.OUTDOOR
    FacilityType.EXPERIENCE -> NetworkService.EXPERIENCE
    FacilityType.MEDICAL -> NetworkService.MEDICAL
    FacilityType.LIBRARY -> NetworkService.LIBRARY
}
