package com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.kidsandseoul.R
import com.ki960213.kidsandseoul.databinding.ItemAllAdministrativeDongBinding

class AdministrativeDongViewHolder(
    parent: ViewGroup,
    onAdministrativeDongClick: (administrativeDongId: Long) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_all_administrative_dong, parent, false)
) {

    private val binding = ItemAllAdministrativeDongBinding.bind(itemView)

    init {
        binding.onAdministrativeDongClick = onAdministrativeDongClick
    }

    fun bind(uiState: AdministrativeDongUiState) {
        binding.uiState = uiState
    }
}
