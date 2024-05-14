package com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemReviewImageBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class ReviewImageAdapter : SliderViewAdapter<ReviewImageViewHolder>() {

    private var imageUrls: List<String> = emptyList()

    override fun getCount(): Int = imageUrls.size

    override fun onCreateViewHolder(parent: ViewGroup): ReviewImageViewHolder =
        ReviewImageViewHolder(parent)

    override fun onBindViewHolder(viewHolder: ReviewImageViewHolder, position: Int) {
        viewHolder.bind(imageUrls[position])
    }

    fun submitList(imageUrls: List<String>) {
        this.imageUrls = imageUrls
        notifyDataSetChanged()
    }
}

class ReviewImageViewHolder(
    parent: ViewGroup,
) : SliderViewAdapter.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_review_image, parent, false)
) {

    private val binding = ItemReviewImageBinding.bind(itemView)

    fun bind(imageUrl: String) {
        binding.imageUrl = imageUrl
    }
}
