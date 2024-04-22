package com.ki960213.kidsandseoul.presentation.ui.postdetail.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ki960213.kidsandseoul.presentation.ui.postdetail.uistate.PostDetailItemUiState

class PostDetailItemsAdapter(
    private val onProfileUiClick: (authorId: String) -> Unit,
    private val onLikeToggle: (postId: String, willLike: Boolean) -> Unit,
    private val onDeleteButtonClick: (commentId: String) -> Unit,
    private val onChildCommentButtonClick: (commentId: String) -> Unit,
) : ListAdapter<PostDetailItemUiState, PostDetailItemViewHolder>(PostDetailItemDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PostDetailItemViewHolder = when (viewType) {
        PostDetailItemUiState.ViewType.POST.ordinal -> PostDetailViewHolder(
            parent = parent,
            onProfileUiClick = onProfileUiClick,
            onLikeToggle = onLikeToggle,
        )

        PostDetailItemUiState.ViewType.COMMENT.ordinal -> CommentViewHolder(
            parent = parent,
            onProfileUiClick = onProfileUiClick,
            onDeleteButtonClick = onDeleteButtonClick,
            onChildCommentButtonClick = onChildCommentButtonClick,
        )

        else -> throw AssertionError("그럴리 없음")
    }

    override fun onBindViewHolder(holder: PostDetailItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = getItem(position).viewType.ordinal
}
