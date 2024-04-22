package com.ki960213.kidsandseoul.presentation.common.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes

private const val PACKAGE = "package"

private var toast: Toast? = null

fun Context.showToast(text: String) {
    if (toast != null) toast!!.cancel()
    toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
    toast!!.show()
}

fun Context.showToast(@StringRes textResId: Int) {
    showToast(getString(textResId))
}

fun Context.navigateToApplicationDetailSetting(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts(PACKAGE, packageName, null)
    }
    launcher.launch(intent)
}
