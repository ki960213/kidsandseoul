package com.ki960213.domain.review.model

class StarPoint(val value: Int) {

    init {
        require(isAvailablePoint(value)) { "별점은 ${MIN_POINT}에서 ${MAX_POINT}점 사이여야 합니다." }
    }

    companion object {

        private const val MIN_POINT = 1
        private const val MAX_POINT = 5

        fun isAvailablePoint(point: Int): Boolean = point in MIN_POINT..MAX_POINT
    }
}
