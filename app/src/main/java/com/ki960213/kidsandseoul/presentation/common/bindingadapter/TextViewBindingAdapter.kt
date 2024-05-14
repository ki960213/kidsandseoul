package com.ki960213.kidsandseoul.presentation.common.bindingadapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.facility.model.ChildCareFacility
import com.ki960213.domain.facility.model.ChildCareFacilityType
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.KidsCafe
import com.ki960213.domain.facility.model.OtherFacility
import com.ki960213.domain.facility.model.OtherFacilityType
import com.ki960213.kidsandseoul.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.round

private const val DATE_FORMAT = "yyyy. MM. dd"
private const val TIME_FORMAT = "hh:mm"

@BindingAdapter("app:text")
fun TextView.setDateTimeText(dateTime: LocalDateTime?) {
    if (dateTime == null) return
    val currentDateTime = LocalDateTime.now()
    text = when {
        dateTime.year < currentDateTime.year ->
            dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

        ChronoUnit.HOURS.between(dateTime, currentDateTime) > 24 ->
            dateTime.format(DateTimeFormatter.ofPattern("MM.dd"))

        ChronoUnit.HOURS.between(dateTime, currentDateTime) in 1..24 ->
            "${ChronoUnit.HOURS.between(dateTime, currentDateTime)}시간 전"

        else -> "${ChronoUnit.MINUTES.between(dateTime, currentDateTime)}분 전"
    }
}

@BindingAdapter("app:text")
fun TextView.setDateText(date: LocalDate?) {
    text = date?.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
}

@BindingAdapter("app:text")
fun TextView.setTimeText(time: LocalTime?) {
    text = time?.format(DateTimeFormatter.ofPattern(TIME_FORMAT))
}

@SuppressLint("SetTextI18n")
@BindingAdapter("app:administrativeDongText")
fun TextView.setAdministrativeText(administrativeDong: AdministrativeDong?) {
    if (administrativeDong == null) return
    text = "서울특별시 ${administrativeDong.borough.name} ${administrativeDong.name}"
}

@BindingAdapter("app:font")
fun TextView.setFont(fontType: FontType) {
    typeface = ResourcesCompat.getFont(context, fontType.fontRes)
}

enum class FontType(@FontRes val fontRes: Int) {
    PRETENDARD_SEMI_BOLD(R.font.pretendard_semi_bold),
    PRETENDARD_MEDIUM(R.font.pretendard_medium),
    PRETENDARD_REGULAR(R.font.pretendard_regular)
}

@BindingAdapter("android:text")
fun TextView.setNumberText(numberText: Int) {
    text = numberText.toString()
}

@BindingAdapter("app:drawableEndCompat")
fun TextView.setDrawableEndCompat(drawable: Drawable) {
    setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
}

@BindingAdapter("app:facilityService")
fun TextView.setService(facility: Facility?) {
    if (facility == null) return
    val stringRes = when (facility) {
        is ChildCareFacility -> when (facility.childCareFacilityType) {
            ChildCareFacilityType.OUR_NEIGHBORHOOD_GROWING_CENTER -> R.string.all_our_neighbor_growing_center
            ChildCareFacilityType.CO_PARENTING_ROOM -> R.string.all_co_parenting_room
            ChildCareFacilityType.LOCAL_CHILDREN_CENTER -> R.string.all_local_children_center
            ChildCareFacilityType.CO_PARENTING_SHARING_CENTER -> R.string.all_co_parenting_sharing_center
        }

        is KidsCafe -> R.string.all_kids_cafe
        is OtherFacility -> when (facility.type) {
            OtherFacilityType.OUTDOOR -> R.string.all_outdoor
            OtherFacilityType.EXPERIENCE -> R.string.all_experience
            OtherFacilityType.MEDICAL -> R.string.all_medical
            OtherFacilityType.LIBRARY -> R.string.all_library
        }
    }
    text = context.getString(stringRes)
}

@BindingAdapter("app:starPointAvg")
fun TextView.setStarPoint(starPointAvg: Double?) {
    if (starPointAvg == null) return
    text = (round(starPointAvg * 10) / 10).toString()
}
