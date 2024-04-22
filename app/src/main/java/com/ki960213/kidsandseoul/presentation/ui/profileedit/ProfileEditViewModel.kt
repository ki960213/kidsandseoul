package com.ki960213.kidsandseoul.presentation.ui.profileedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.administrativedong.model.Borough
import com.ki960213.domain.kid.repository.KidRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.data.repository.DefaultAdministrativeDongRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.kidadd.administrativedongs.AdministrativeDongUiState
import com.ki960213.kidsandseoul.presentation.ui.kidadd.boroughs.BoroughUiState
import com.ki960213.kidsandseoul.presentation.ui.profileedit.kids.KidAddUiState
import com.ki960213.kidsandseoul.presentation.ui.profileedit.kids.KidUiState
import com.ki960213.kidsandseoul.presentation.ui.profileedit.kids.KidsItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val kidRepository: KidRepository,
    private val administrativeDongRepository: DefaultAdministrativeDongRepository,
) : BaseViewModel() {

    val parentId: String = savedStateHandle[ProfileEditFragment.KEY_PARENT_ID]
        ?: throw IllegalArgumentException("프로필 수정 화면으로 이동할 때 부모 아이디 안넘겼음")

    val user: StateFlow<JoinedUser?> = userRepository.getUser(parentId)
        .filter { it is JoinedUser }
        .map { it as JoinedUser }
        .viewModelStateIn(initialValue = null)

    val tempName: MutableStateFlow<String> = user.filterNotNull()
        .map { it.name }
        .viewModelMutableStateIn(initialValue = "")

    val isNameChanged: StateFlow<Boolean> =
        combine(user.filterNotNull(), tempName) { user, tempName ->
            user.name != tempName
        }.viewModelStateIn(initialValue = false)

    private val _administrativeDongs: MutableStateFlow<Map<Borough, List<AdministrativeDong>>> =
        MutableStateFlow(emptyMap())
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
        user.filterNotNull()
            .map { it.livingDong }
            .viewModelMutableStateIn(initialValue = null)
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

    val isLivingDongChanged: StateFlow<Boolean> =
        combine(user.filterNotNull(), selectedAdministrativeDong.filterNotNull()) { user, dong ->
            user.livingDong != dong
        }.viewModelStateIn(initialValue = false)

    private val _kids: MutableStateFlow<List<KidsItemUiState>> = kidRepository.getKids(parentId)
        .map { kids ->
            kids.kids.mapIndexed { index, kid ->
                KidUiState(
                    parentId = parentId,
                    order = index + 1,
                    kid = kid,
                )
            } + KidAddUiState
        }
        .viewModelMutableStateIn(initialValue = emptyList())
    val kids: StateFlow<List<KidsItemUiState>> = _kids.asStateFlow()

    init {
        fetchAdministrativeDongs()
    }

    private fun fetchAdministrativeDongs() = viewModelScope.launch {
        _administrativeDongs.value = administrativeDongRepository.getAdministrativeDongs()
            .let { it.values.groupBy { dong -> dong.borough } }
    }

    fun saveName() = viewModelScope.launch {
        userRepository.changeName(parentId, tempName.value)
    }

    fun changeProfileImage(profileImageFile: File) = viewModelScope.launch {
        val user = user.value ?: return@launch
        userRepository.changeProfileImageUrl(user.id, profileImageFile)
    }

    fun deleteKid(kidId: String) = viewModelScope.launch {
        kidRepository.deleteKid(parentId, kidId)
    }

    fun selectBorough(boroughId: Long) {
        selectedBorough.value = _boroughs.value.firstOrNull { it.id == boroughId }
    }

    fun selectAdministrativeDong(administrativeDongId: Long) {
        selectedAdministrativeDong.value = currentAdministrativeDongs.value
            .firstOrNull { it.id == administrativeDongId }
    }

    fun saveLivingDong() = viewModelScope.launch {
        val dongId = selectedAdministrativeDong.value?.id ?: return@launch
        userRepository.changeLivingDong(parentId, dongId)
    }
}
