package com.ki960213.kidsandseoul.presentation.ui.profileedit

import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.presentation.common.view.ImagePermissionDialog
import java.io.File

class ProfileEditImagePermissionDialog : ImagePermissionDialog() {

    private val viewModel: ProfileEditViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onImageFileReceive(imageFile: File) {
        viewModel.changeProfileImage(imageFile)
    }
}
