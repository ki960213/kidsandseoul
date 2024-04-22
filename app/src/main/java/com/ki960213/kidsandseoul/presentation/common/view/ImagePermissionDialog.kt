package com.ki960213.kidsandseoul.presentation.common.view

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateToApplicationDetailSetting
import com.ki960213.kidsandseoul.presentation.common.util.isImagePermissionGrantedCompat
import com.ki960213.kidsandseoul.presentation.common.util.toFile
import java.io.File

abstract class ImagePermissionDialog : ConfirmDialogFragment() {

    final override val title: String
        get() = "이미지 권한이 거부되었어요"
    final override val message: String
        get() = "이미지 권한 허용을 위해 설정창으로 이동하시겠습니까?"

    private val permissionSettingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (requireContext().isImagePermissionGrantedCompat()) navigateToGallery()
        }

    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageUri = it.data?.data ?: return@registerForActivityResult
                val imageFile = imageUri.toFile(requireContext())
                onImageFileReceive(imageFile)
            }
        }

    private fun navigateToGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        albumLauncher.launch(Intent.createChooser(intent, "갤러리 종류를 선택하세요."))
    }

    abstract fun onImageFileReceive(imageFile: File)

    final override fun action() {
        requireContext().navigateToApplicationDetailSetting(permissionSettingLauncher)
    }
}
