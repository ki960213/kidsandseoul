package com.ki960213.kidsandseoul.presentation.common.bindingadapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ki960213.domain.facility.model.ChildCareFacility
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.KidsCafe
import com.ki960213.domain.facility.model.OtherFacility
import com.ki960213.domain.facility.model.OtherFacilityType
import com.ki960213.kidsandseoul.R
import com.stfalcon.imageviewer.StfalconImageViewer

@BindingAdapter(
    "app:imageUrl",
    "app:errorImage",
    "app:isCircle",
    "app:canZoomIn",
    requireAll = false,
)
fun ImageView.setImage(
    imageUrl: String?,
    errorImage: Drawable?,
    isCircle: Boolean = false,
    canZoomIn: Boolean = false,
) {
    Glide.with(this)
        .load(imageUrl)
        .error(errorImage)
        .customTransform(isCircle = isCircle)
        .into(this)

    if (canZoomIn) {
        makeItPossibleToZoomInOnClick(
            imageUrl = imageUrl,
            defaultImage = errorImage,
        )
    }
}

private fun RequestBuilder<Drawable>.customTransform(
    isCircle: Boolean = false,
): RequestBuilder<Drawable> = when {
    isCircle -> transform(CenterCrop(), CircleCrop())
    else -> this
}

private fun ImageView.makeItPossibleToZoomInOnClick(
    imageUrl: String?,
    defaultImage: Drawable?,
) {
    setOnClickListener {
        StfalconImageViewer.Builder(this.context, listOf(imageUrl)) { view, image ->
            Glide.with(this.context)
                .load(image)
                .error(defaultImage)
                .into(view)
        }.show()
    }
}

@BindingAdapter("app:all_facilityServiceImage")
fun ImageView.setFacilityServiceImage(facility: Facility?) {
    if (facility == null) return
    val drawableRes = when (facility) {
        is ChildCareFacility -> R.drawable.img_all_child_care
        is KidsCafe -> R.drawable.img_all_kids_cafe
        is OtherFacility -> when (facility.type) {
            OtherFacilityType.OUTDOOR -> R.drawable.img_all_outdoor
            OtherFacilityType.EXPERIENCE -> R.drawable.img_all_experience
            OtherFacilityType.MEDICAL -> R.drawable.img_all_medical
            OtherFacilityType.LIBRARY -> R.drawable.img_all_library
        }
    }
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    setImageDrawable(drawable)
}
