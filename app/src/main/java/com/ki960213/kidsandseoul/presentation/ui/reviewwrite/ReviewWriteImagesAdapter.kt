package com.ki960213.kidsandseoul.presentation.ui.reviewwrite

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemReviewWriteImageBinding
import com.smarteist.autoimageslider.SliderViewAdapter
import java.io.File

class ReviewWriteImagesAdapter(
    private val onAddClick: () -> Unit,
    private val onImageDeleteButtonClick: (imageFile: File) -> Unit,
) : SliderViewAdapter<ReviewImageItemViewHolder>() {

    private val items = mutableListOf<ReviewWriteImageItem>()

    override fun onCreateViewHolder(parent: ViewGroup): ReviewImageItemViewHolder =
        ReviewImageItemViewHolder(
            parent = parent,
            onAddClick = onAddClick,
            onImageDeleteButtonClick = onImageDeleteButtonClick,
        )

    override fun onBindViewHolder(viewHolder: ReviewImageItemViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getCount(): Int = items.size

    fun submitList(list: List<ReviewWriteImageItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}

sealed interface ReviewWriteImageItem {
    data object Add : ReviewWriteImageItem
    data class Image(val image: File) : ReviewWriteImageItem
}

class ReviewImageItemViewHolder(
    parent: ViewGroup,
    onAddClick: () -> Unit,
    onImageDeleteButtonClick: (imageFile: File) -> Unit,
) : SliderViewAdapter.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_review_write_image, parent, false)
) {

    private val binding = ItemReviewWriteImageBinding.bind(itemView)

    init {
        binding.onImageAddButtonClick = onAddClick
        binding.onImageDeleteButtonClick = onImageDeleteButtonClick
    }

    fun bind(reviewWriteImageItem: ReviewWriteImageItem) {
        when (reviewWriteImageItem) {
            ReviewWriteImageItem.Add -> binding.imageFile = null
            is ReviewWriteImageItem.Image -> {
                binding.imageFile = reviewWriteImageItem.image
                Glide.with(binding.ivReviewImage)
                    .load(reviewWriteImageItem.image)
                    .into(binding.ivReviewImage)
            }
        }
    }
}
