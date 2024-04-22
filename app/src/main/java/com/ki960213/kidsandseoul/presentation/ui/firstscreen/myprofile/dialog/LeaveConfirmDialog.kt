package com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.dialog

import androidx.fragment.app.viewModels
import com.ki960213.kidsandseoul.presentation.common.base.ConfirmDialogFragment
import com.ki960213.kidsandseoul.presentation.ui.firstscreen.myprofile.MyProfileViewModel

class LeaveConfirmDialog : ConfirmDialogFragment() {

    private val viewModel: MyProfileViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override val title: String
        get() = "회원 탈퇴"
    override val message: String
        get() = "회원 탈퇴하시면\n데이터는 복구되지 않습니다.\n회원 탈퇴 하시겠습니까?"

    override fun action() {
        viewModel.leave()
    }
}
