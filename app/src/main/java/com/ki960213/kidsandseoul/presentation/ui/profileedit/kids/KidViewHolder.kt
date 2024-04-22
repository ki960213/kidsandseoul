package com.ki960213.kidsandseoul.presentation.ui.profileedit.kids

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemProfileEditKidBinding

class KidViewHolder(
    parent: ViewGroup,
    onDeleteClick: (kidId: String) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_profile_edit_kid, parent, false)
) {

    private val binding = ItemProfileEditKidBinding.bind(itemView)

    init {
        binding.onDeleteButtonClick = onDeleteClick
    }

    fun bind(uiState: KidUiState) {
        binding.uiState = uiState
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("app:profileEditKid_order")
fun TextView.setOrder(order: Int) {
    text = "${order.toKorean()}째 아이"
}

private fun Int.toKorean(): String = when (this) {
    1 -> "첫"
    2 -> "둘"
    3 -> "셋"
    4 -> "넷"
    5 -> "다섯"
    6 -> "여섯"
    7 -> "일곱"
    8 -> "여덟"
    9 -> "아홉"
    10 -> "열"
    else -> toString() + "번"
}
