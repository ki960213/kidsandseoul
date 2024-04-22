package com.ki960213.kidsandseoul.data.firebase.statistics

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class FacilityStatisticsDocument(
    @JvmField @PropertyName(FIELD_HOT_POINT) val hotPoint: Int = 0,
) {

    companion object {

        const val FIELD_HOT_POINT = "hotPoint"
    }
}
