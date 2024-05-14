package com.ki960213.kidsandseoul.presentation.ui.profileedit

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentProfileEditBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.navigateSafely
import com.ki960213.kidsandseoul.presentation.common.util.onImagePermissionCompat
import com.ki960213.kidsandseoul.presentation.common.util.toFile
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongsAdapter
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughsAdapter
import com.ki960213.kidsandseoul.presentation.ui.profileedit.kids.KidsAdapter
import com.ki960213.kidsandseoul.presentation.ui.profileedit.kids.KidsItemUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>(R.layout.fragment_profile_edit) {

    private val viewModel: ProfileEditViewModel by viewModels()

    private val imagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) navigateToGallery()
    }

    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val imageUri = it.data?.data ?: return@registerForActivityResult
                val imageFile = imageUri.toFile(requireContext())
                viewModel.changeProfileImage(profileImageFile = imageFile)
            }
        }

    private fun navigateToGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        albumLauncher.launch(Intent.createChooser(intent, getString(R.string.all_choose_gallery_type)))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupRecyclerViews()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { findNavController().popBackStack() }
        binding.onProfileImageClick = ::startToEditProfileImage
    }

    private fun startToEditProfileImage() {
        requireActivity().onImagePermissionCompat(
            onGranted = ::navigateToGallery,
            onShouldShowRequestPermissionRationale = { showNavigateToDetailSettingDialog() },
            onDenied = imagePermissionLauncher::launch,
        )
    }

    private fun showNavigateToDetailSettingDialog() {
        ProfileEditImagePermissionDialog().show(childFragmentManager, null)
    }

    private fun setupRecyclerViews() {
        binding.rvProfileEditKids.adapter = KidsAdapter(
            onKidAddClick = ::navigateToKidAdd,
            onKidDeleteClick = { viewModel.deleteKid(it) }
        )
        binding.rvProfileEditBoroughs.adapter = BoroughsAdapter(viewModel::selectBorough)
        binding.rvProfileEditAdministrativeDongs.adapter =
            AdministrativeDongsAdapter(viewModel::selectAdministrativeDong)
    }

    private fun navigateToKidAdd() {
        findNavController().navigateSafely(
            ProfileEditFragmentDirections.actionProfileEditFragmentToKidAddFragment(viewModel.parentId)
        )
    }

    companion object {

        const val KEY_PARENT_ID = "parentId"
    }
}

@BindingAdapter("app:profileEdit_kids")
fun RecyclerView.setKids(kids: List<KidsItemUiState>) {
    (adapter as KidsAdapter).submitList(kids)
}
