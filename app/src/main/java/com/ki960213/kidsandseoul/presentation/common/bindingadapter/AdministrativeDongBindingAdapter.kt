package com.ki960213.kidsandseoul.presentation.common.bindingadapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongsAdapter
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughsAdapter

@SuppressLint("SetTextI18n")
@BindingAdapter("app:all_selectedAdministrativeDong")
fun TextView.setSelectedRegion(selectedAdministrativeDong: AdministrativeDong?) {
    if (selectedAdministrativeDong == null) return
    text = "서울 ${selectedAdministrativeDong.borough.name} ${selectedAdministrativeDong.name}"
}

@BindingAdapter("app:all_boroughs")
fun RecyclerView.setBoroughs(boroughs: List<BoroughUiState>) {
    (adapter as BoroughsAdapter).submitList(boroughs)
}

@BindingAdapter("app:all_administrativeDongs")
fun RecyclerView.setAdministrativeDongs(administrativeDongs: List<AdministrativeDongUiState>) {
    (adapter as AdministrativeDongsAdapter).submitList(administrativeDongs)
}
