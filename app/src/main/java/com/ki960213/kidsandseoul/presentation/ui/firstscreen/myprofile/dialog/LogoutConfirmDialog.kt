package com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.dialog

import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.MyProfileViewModel

class LogoutConfirmDialog : ConfirmDialogFragment() {

    private val viewModel: MyProfileViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val title: String
        get() = "로그아웃"
    override val message: String
        get() = "로그아웃 하시겠습니까?"

    override fun action() {
        viewModel.logout()
    }
}
