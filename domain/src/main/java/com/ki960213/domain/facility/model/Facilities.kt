package com.ki960213.domain.facility.model

/**
 * 시설 목록의 도메인 모델
 *
 * @param[allCount] 가지고 있는 [Facility] 객체의 개수가 아니라 전체 개수(서버에 저장된)
 * @param[value] [Facility] 목록
 */
data class Facilities(val allCount: Int, val value: List<Facility>) {

    fun apply(condition: FacilityFilterCondition): Facilities {
        val result = value.filter { condition.isSatisfiedBy(it) }
        return Facilities(allCount = result.size, value = result)
    }

    fun sortedByReviewCount(isDescending: Boolean): Facilities = if (isDescending) {
        this.copy(value = value.sortedByDescending { it.reviewCount })
    } else {
        this.copy(value = value.sortedBy { it.reviewCount })
    }
}
