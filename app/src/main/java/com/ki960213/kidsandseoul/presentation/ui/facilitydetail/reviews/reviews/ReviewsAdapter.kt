package com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.review.model.Review
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemReviewBinding
import com.smarteist.autoimageslider.SliderView

class ReviewsAdapter(
    private val onProfileClick: (userId: String) -> Unit,
    private val onFacilityNameClick: (facilityId: Long) -> Unit,
    private val onDeleteButtonClick: (reviewId: String) -> Unit,
) : ListAdapter<ReviewUiState, ReviewViewHolder>(ReviewDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(parent, onProfileClick, onFacilityNameClick, onDeleteButtonClick)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

data class ReviewUiState(
    val authorId: String,
    val authorProfileImageUrl: String,
    val authorName: String,
    val isDeletable: Boolean,
    val facility: Facility,
    val review: Review,
) {

    constructor(
        author: JoinedUser,
        isDeletable: Boolean,
        facility: Facility,
        review: Review,
    ) : this(
        authorId = author.id,
        authorProfileImageUrl = author.profileImageUrl,
        authorName = author.name,
        isDeletable = isDeletable,
        facility = facility,
        review = review,
    )
}

class ReviewViewHolder(
    parent: ViewGroup,
    onProfileClick: (userId: String) -> Unit,
    onFacilityNameClick: (facilityId: Long) -> Unit,
    onDeleteButtonClick: (reviewId: String) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
) {

    private val binding = ItemReviewBinding.bind(itemView)

    init {
        binding.onProfileUiClick = onProfileClick
        binding.onFacilityNameClick = onFacilityNameClick
        binding.onDeleteButtonClick = onDeleteButtonClick
        binding.sliderReviewItemImages.setSliderAdapter(ReviewImageAdapter(), false)
    }

    fun bind(uiState: ReviewUiState) {
        binding.uiState = uiState
    }
}

@BindingAdapter("app:all_reviewImages")
fun SliderView.setReviewImages(reviewImages: List<String>?) {
    if (reviewImages == null) return
    (sliderAdapter as ReviewImageAdapter).submitList(reviewImages)
}

class ReviewDiffUtil : DiffUtil.ItemCallback<ReviewUiState>() {

    override fun areItemsTheSame(oldItem: ReviewUiState, newItem: ReviewUiState): Boolean {
        return oldItem.review.id == newItem.review.id
    }

    override fun areContentsTheSame(oldItem: ReviewUiState, newItem: ReviewUiState): Boolean {
        return oldItem == newItem
    }
}
