package com.ki960213.kidsandseoul.presentation.ui

import android.content.Intent
import android.net.Uri
import com.ki960213.kidsandseoul.presentation.common.base.InfoDialogFragment

class UpdateInfoDialog : InfoDialogFragment() {

    override val title: String
        get() = "업데이트"
    override val message: String
        get() = "새로운 버전이 출시되었어요!\n지금 바로 스토어로 이동해보세요!"

    override fun action() {
        requireActivity().packageName
        val intent = Intent(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.parse("https://play.google.com/store/apps/details?id=${requireActivity().packageName}"))
        startActivity(intent)
        requireActivity().finishAffinity()
    }
}
