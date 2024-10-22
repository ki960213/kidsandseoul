package com.ki960213.kidsandseoul.presentation.common.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

private const val HIGHEST_COMPRESS_QUALITY = 100

fun Uri.toFile(context: Context): File = toBitmap(context).toFile(context)

private fun Uri.toBitmap(context: Context): Bitmap = context.contentResolver
    .openInputStream(this)
    .use { BitmapFactory.decodeStream(it) }

private fun Bitmap.toFile(context: Context): File {
    val tempFile = File.createTempFile("imageFile", ".jpg", context.cacheDir)
    FileOutputStream(tempFile).use {
        compress(Bitmap.CompressFormat.JPEG, HIGHEST_COMPRESS_QUALITY, it)
    }
    return tempFile
}

fun Activity.onImagePermissionCompat(
    onGranted: () -> Unit,
    onShouldShowRequestPermissionRationale: () -> Unit,
    onDenied: (String) -> Unit,
) {
    onImagePermissionCompat(
        getImagePermissionCompat(),
        onGranted,
        onShouldShowRequestPermissionRationale,
        onDenied,
    )
}

private fun getImagePermissionCompat(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}

fun Activity.onImagePermissionCompat(
    permission: String,
    onGranted: () -> Unit,
    onShouldShowRequestPermissionRationale: () -> Unit,
    onDenied: (String) -> Unit,
) {
    when {
        isImagePermissionGrantedCompat() -> onGranted()

        shouldShowRequestPermissionRationale(permission) -> onShouldShowRequestPermissionRationale()

        else -> onDenied(permission)
    }
}

fun Context.isImagePermissionGrantedCompat(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        getImagePermissionCompat(),
    ) == PackageManager.PERMISSION_GRANTED
}
