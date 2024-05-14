package com.ki960213.kidsandseoul.presentation.ui.reviewwrite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.FragmentReviewWriteBinding
import com.ki960213.kidsandseoul.presentation.common.base.BaseFragment
import com.ki960213.kidsandseoul.presentation.common.extension.repeatOnStarted
import com.ki960213.kidsandseoul.presentation.common.util.onImagePermissionCompat
import com.ki960213.kidsandseoul.presentation.common.util.toFile
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ReviewWriteFragment : BaseFragment<FragmentReviewWriteBinding>(R.layout.fragment_review_write) {

    private val viewModel: ReviewWriteViewModel by viewModels()

    private val imagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) navigateToGallery()
    }

    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageUri = it.data?.data ?: return@registerForActivityResult
                val imageFile = imageUri.toFile(requireContext())
                viewModel.addImage(imageFile)
            }
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showExitConfirmDialog()
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
        setupWindowInsetsListener(view)
        setupBackPressedDispatcher()
        setupImageSlider()

        observeIsChanged()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onBackButtonClick = { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.onRegisterButtonClick = ::handleRegisterButtonClick
    }

    private fun handleRegisterButtonClick() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.register().join()
            findNavController().popBackStack()
        }
    }

    private fun setupBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun showExitConfirmDialog() {
        ReviewWriteExitConfirmDialog().show(childFragmentManager, null)
    }

    private fun setupWindowInsetsListener(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val navigatorBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            if (imeVisible) {
                binding.root.setPadding(0, 0, 0, imeHeight - navigatorBarHeight)
            } else {
                binding.root.setPadding(0, 0, 0, 0)
            }
            insets
        }
    }

    private fun setupImageSlider() {
        val adapter = ReviewWriteImagesAdapter(
            onAddClick = ::startToEditProfileImage,
            onImageDeleteButtonClick = viewModel::deleteImage,
        )
        binding.sliderReviewWriteImages.setSliderAdapter(adapter, false)
    }

    private fun startToEditProfileImage() {
        requireActivity().onImagePermissionCompat(
            onGranted = ::navigateToGallery,
            onShouldShowRequestPermissionRationale = { showNavigateToDetailSettingDialog() },
            onDenied = { imagePermissionLauncher.launch(it) },
        )
    }

    private fun showNavigateToDetailSettingDialog() {
        ReviewWriteImagePermissionDialog().show(childFragmentManager, null)
    }

    private fun observeIsChanged() {
        repeatOnStarted {
            viewModel.isChanged.collect { onBackPressedCallback.isEnabled = it }
        }
    }

    companion object {

        const val KEY_FACILITY_ID = "facilityId"
    }
}

@BindingAdapter("app:reviewWrite_images")
fun SliderView.setReviewWriteImages(imageUrls: List<File>) {
    val items: MutableList<ReviewWriteImageItem> = imageUrls.map { ReviewWriteImageItem.Image(it) }.toMutableList()
    // TODO: 이미지 10장 제한 같은 비즈니스 로직은 뷰 모델이나 도메인 레이어에서 하는 게 좋긴 함.
    if (items.size < 10) items += ReviewWriteImageItem.Add
    (sliderAdapter as ReviewWriteImagesAdapter).submitList(items)

    // 어댑터 다시 설정해야 이미지 indicator가 보이는 버그 때문
    val previousPosition = currentPagePosition
    setInfiniteAdapterEnabled(false)
    currentPagePosition = previousPosition
}
