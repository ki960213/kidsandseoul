package com.ki960213.kidsandseoul.presentation.ui.kidadd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.administrativedong.model.Borough
import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.kid.repository.KidRepository
import com.ki960213.domain.user.model.Sex
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class KidAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    administrativeDongRepository: AdministrativeDongRepository,
    private val kidRepository: KidRepository,
) : BaseViewModel() {

    private val parentId: String = savedStateHandle[KidAddFragment.KEY_PARENT_ID]
        ?: throw IllegalArgumentException("아이 추가 화면으로 이동할 때 부모 아이디 안넘겼음")

    val name: MutableStateFlow<String> = MutableStateFlow("")

    private val _selectedSex: MutableStateFlow<Sex?> = MutableStateFlow(null)
    val selectedSex: StateFlow<Sex?> = _selectedSex

    private val _selectedBirthDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    val selectedBirthDate: StateFlow<LocalDate> = _selectedBirthDate

    private val _administrativeDongs: StateFlow<Map<Borough, List<AdministrativeDong>>> =
        administrativeDongRepository.administrativeDongs
            .map { it.values.groupBy { dong -> dong.borough } }
            .viewModelStateIn(initialValue = emptyMap())

    private val _boroughs: StateFlow<List<Borough>> = _administrativeDongs.map { it.keys.toList() }
        .viewModelStateIn(initialValue = emptyList())
    private val selectedBorough: MutableStateFlow<Borough?> = MutableStateFlow(null)
    val boroughs: StateFlow<List<BoroughUiState>> = combine(
        _boroughs, selectedBorough,
    ) { boroughs, selectedBorough ->
        boroughs.map {
            BoroughUiState(
                isSelected = it.id == selectedBorough?.id,
                borough = it
            )
        }
    }.viewModelStateIn(initialValue = emptyList())

    private val currentAdministrativeDongs: StateFlow<List<AdministrativeDong>> = combine(
        _administrativeDongs, selectedBorough
    ) { dongs, selectedBorough ->
        dongs[selectedBorough] ?: emptyList()
    }.viewModelStateIn(initialValue = emptyList())
    val selectedAdministrativeDong: MutableStateFlow<AdministrativeDong?> =
        MutableStateFlow(null)
    val administrativeDongs: StateFlow<List<AdministrativeDongUiState>> = combine(
        currentAdministrativeDongs, selectedAdministrativeDong
    ) { dongs, selectedDongs ->
        dongs.map {
            AdministrativeDongUiState(
                isSelected = it.id == selectedDongs?.id,
                administrativeDong = it,
            )
        }
    }.viewModelStateIn(initialValue = emptyList())

    val isComplete: StateFlow<Boolean> = combine(
        name,
        _selectedSex,
        selectedAdministrativeDong
    ) { name, sex, administrativeDong ->
        name.isNotBlank() && sex != null && administrativeDong != null
    }.viewModelStateIn(initialValue = false)

    fun selectSex(sex: Sex) {
        _selectedSex.value = sex
    }

    fun selectBirthDate(birthDate: LocalDate) {
        _selectedBirthDate.value = birthDate
    }

    fun selectBorough(boroughId: Long) {
        selectedBorough.value = _boroughs.value.firstOrNull { it.id == boroughId }
    }

    fun selectAdministrativeDong(administrativeDongId: Long) {
        selectedAdministrativeDong.value =
            currentAdministrativeDongs.value.firstOrNull { it.id == administrativeDongId }
    }

    fun addKid() = viewModelScope.launch {
        kidRepository.addKid(
            parentId = parentId,
            name = name.value,
            sex = selectedSex.value ?: return@launch,
            birthDate = selectedBirthDate.value,
            livingDong = selectedAdministrativeDong.value ?: return@launch,
        )
    }
}
