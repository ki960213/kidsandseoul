package com.ki960213.kidsandseoul.presentation.ui

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import com.ki960213.kidsandseoul.presentation.common.extension.showToast

class SafeExitCallback(private val activity: Activity) : OnBackPressedCallback(true) {

    private var backPressedTime: Long = 0

    override fun handleOnBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < ASK_AGAIN_EXIT_DURATION) {
            activity.finish()
        } else {
            backPressedTime = currentTime
            activity.showToast("한 번 더 누르면 종료됩니다.")
        }
    }

    companion object {

        private const val ASK_AGAIN_EXIT_DURATION = 2_000
    }
}
