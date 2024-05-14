package com.ki960213.kidsandseoul.presentation.ui

import android.content.Intent
import android.net.Uri
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.presentation.common.base.InfoDialogFragment

class UpdateInfoDialog : InfoDialogFragment() {

    override val title: String
        get() = getString(R.string.update_info_dialog_title)
    override val message: String
        get() = getString(R.string.update_info_dialog_message)

    override fun action() {
        requireActivity().packageName
        val intent = Intent(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.parse("https://play.google.com/store/apps/details?id=${requireActivity().packageName}"))
        startActivity(intent)
        requireActivity().finishAffinity()
    }
}
