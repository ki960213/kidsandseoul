package com.ki960213.kidsandseoul.presentation.common.bindingadapter

import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("app:onRefresh")
fun SwipeRefreshLayout.setOnRefresh(listener: SwipeRefreshLayout.OnRefreshListener) {
    setOnRefreshListener(listener)
}

@BindingAdapter("app:swipeRefreshColor")
fun SwipeRefreshLayout.setSwipeRefreshColor(@ColorInt color: Int) {
    setColorSchemeColors(color)
}
