package com.ki960213.kidsandseoul.presentation.ui.profile

import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.presentation.common.base.InfoDialogFragment

class LeavedUserDialog : InfoDialogFragment() {

    override val title: String
        get() = "탈퇴한 사용자"
    override val message: String
        get() = "탈퇴한 사용자입니다."

    override fun action() {
        requireParentFragment().findNavController().popBackStack()
    }
}
