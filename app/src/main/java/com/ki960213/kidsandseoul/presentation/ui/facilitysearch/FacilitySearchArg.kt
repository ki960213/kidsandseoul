package com.ki960213.kidsandseoul.presentation.ui.facilitysearch

import android.os.Parcelable
import androidx.annotation.Keep
import com.ki960213.kidsandseoul.presentation.common.uistate.ServiceUiState
import kotlinx.parcelize.Parcelize

@Keep
sealed interface FacilitySearchArg : Parcelable {

    @Parcelize
    data class Service(val type: ServiceUiState) : FacilitySearchArg

    @Parcelize
    data class Keyword(val keyword: String) : FacilitySearchArg
}
