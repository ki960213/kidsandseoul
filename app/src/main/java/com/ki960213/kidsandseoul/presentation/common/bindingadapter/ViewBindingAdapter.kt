package com.ki960213.kidsandseoul.presentation.common.bindingadapter

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("app:isVisible")
fun View.setIsVisible(visible: Boolean) {
    isVisible = visible
}
