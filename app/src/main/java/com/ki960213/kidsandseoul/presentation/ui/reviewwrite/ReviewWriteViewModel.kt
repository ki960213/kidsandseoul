package com.ki960213.kidsandseoul.presentation.ui.reviewwrite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.domain.review.model.StarPoint
import com.ki960213.domain.review.repository.ReviewRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ReviewWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val userRepository: UserRepository,
    facilityRepository: FacilityRepository,
    private val reviewRepository: ReviewRepository,
    @ApplicationScope private val externalScope: CoroutineScope,
) : BaseViewModel() {

    private val facilityId: Long = savedStateHandle[ReviewWriteFragment.KEY_FACILITY_ID]
        ?: throw IllegalArgumentException()

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = authRepository.loginUserId
        .filterNotNull()
        .flatMapLatest { userRepository.getUser(it) }
        .filterIsInstance<JoinedUser>()
        .viewModelStateIn(initialValue = null)

    val facility: StateFlow<Facility?> = facilityRepository.getFacility(facilityId)
        .viewModelStateIn(initialValue = null)

    private val _starPoint: MutableStateFlow<Int> = MutableStateFlow(0)
    val starPoint: StateFlow<Int> = _starPoint.asStateFlow()

    private val _images: MutableStateFlow<List<File>> = MutableStateFlow(emptyList())
    val images: StateFlow<List<File>> = _images.asStateFlow()

    val content: MutableStateFlow<String> = MutableStateFlow("")

    val isChanged: StateFlow<Boolean> = combine(content, _images) { content, images ->
        content.isNotBlank() || images.isNotEmpty()
    }.viewModelStateIn(initialValue = false)

    val canRegister: StateFlow<Boolean> = combine(
        loginUser,
        content,
        _starPoint
    ) { user, review, starPoint ->
        user != null && review.isNotBlank()
                && review.length <= MAX_REVIEW_LENGTH
                && StarPoint.isAvailablePoint(starPoint)
    }.viewModelStateIn(initialValue = false)

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun setStarPoint(point: Int) {
        _starPoint.value = point
    }

    fun addImage(imageFile: File) = viewModelScope.launch {
        _images.value += imageFile
    }

    fun deleteImage(imageFile: File) = externalScope.launch {
        _images.value -= imageFile
    }

    fun register() = viewModelScope.launch {
        val loginUser = loginUser.value ?: return@launch
        _isLoading.value = true
        reviewRepository.createReview(
            authorId = loginUser.id,
            facilityId = facilityId,
            starPoint = StarPoint(starPoint.value),
            content = content.value,
            imageFiles = _images.value,
        )
        _isLoading.value = false
    }

    companion object {

        const val MAX_REVIEW_LENGTH = 500
    }
}
