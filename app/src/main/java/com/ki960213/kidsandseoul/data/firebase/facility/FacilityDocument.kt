package com.ki960213.kidsandseoul.data.firebase.facility

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class FacilityDocument(
    @JvmField @PropertyName(FIELD_ID) val id: Long = 0,
    @JvmField @PropertyName(FIELD_REVIEW_COUNT) val reviewCount: Int = 0,
    @JvmField @PropertyName(FIELD_STAR_POINT_AVG) val starPointAvg: Double = 0.0,
    @JvmField @PropertyName(FIELD_STAR_POINT_COUNT) val starPointCount: Int = 0,
) {

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_REVIEW_COUNT = "reviewCount"
        const val FIELD_STAR_POINT_AVG = "starPointAvg"
        const val FIELD_STAR_POINT_COUNT = "starPointCount"
    }
}
