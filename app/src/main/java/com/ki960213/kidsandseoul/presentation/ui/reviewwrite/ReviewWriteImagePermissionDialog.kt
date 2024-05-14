package com.ki960213.kidsandseoul.presentation.ui.reviewwrite

import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.presentation.common.view.ImagePermissionDialog
import java.io.File

class ReviewWriteImagePermissionDialog : ImagePermissionDialog() {

    private val viewModel: ReviewWriteViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onImageFileReceive(imageFile: File) {
        viewModel.addImage(imageFile)
    }
}
