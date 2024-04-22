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
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val userRepository: UserRepository,
    facilityRepository: FacilityRepository,
    private val reviewRepository: ReviewRepository,
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

    val review: MutableStateFlow<String> = MutableStateFlow("")

    val isChanged: Boolean
        get() = review.value.isNotBlank()

    val canRegister: StateFlow<Boolean> = combine(
        loginUser,
        review,
        _starPoint
    ) { user, review, starPoint ->
        user != null && review.isNotBlank()
                && review.length <= MAX_REVIEW_LENGTH
                && StarPoint.isAvailablePoint(starPoint)
    }.viewModelStateIn(initialValue = false)

    fun setStarPoint(point: Int) {
        _starPoint.value = point
    }

    fun register() = viewModelScope.launch {
        val loginUser = loginUser.value ?: return@launch
        reviewRepository.createReview(
            authorId = loginUser.id,
            facilityId = facilityId,
            starPoint = StarPoint(starPoint.value),
            content = review.value,
            imageUrls = emptyList(),
        )
    }

    companion object {

        const val MAX_REVIEW_LENGTH = 500
    }
}
