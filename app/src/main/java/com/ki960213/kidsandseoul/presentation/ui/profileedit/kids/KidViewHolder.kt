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
    val orderText = when (order) {
        1 -> context.getString(R.string.profile_edit_first_kid_order)
        2 -> context.getString(R.string.profile_edit_second_kid_order)
        3 -> context.getString(R.string.profile_edit_third_kid_order)
        4 -> context.getString(R.string.profile_edit_fourth_kid_order)
        5 -> context.getString(R.string.profile_edit_fifth_kid_order)
        6 -> context.getString(R.string.profile_edit_sixth_kid_order)
        7 -> context.getString(R.string.profile_edit_seventh_kid_order)
        8 -> context.getString(R.string.profile_edit_eighth_kid_order)
        9 -> context.getString(R.string.profile_edit_ninth_kid_order)
        10 -> context.getString(R.string.profile_edit_tenth_kid_order)
        else -> context.getString(R.string.profile_edit_greater_than_tenth_kid_order_format, order)
    }
    text = context.getString(R.string.profile_edit_kid_order_format, orderText)
}
