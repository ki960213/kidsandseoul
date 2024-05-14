package com.ki960213.kidsandseoul.presentation.ui.writtenReviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.facility.model.AllFacilityFilterConditions
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.domain.review.model.Review
import com.ki960213.domain.review.repository.ReviewRepository
import com.ki960213.domain.user.model.JoinedUser
import com.ki960213.domain.user.model.LeavedUser
import com.ki960213.domain.user.repository.UserRepository
import com.ki960213.kidsandseoul.presentation.common.base.BaseViewModel
import com.ki960213.kidsandseoul.presentation.ui.facilitydetail.reviews.reviews.ReviewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WrittenReviewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val facilityRepository: FacilityRepository,
) : BaseViewModel() {

    val authorId: String = savedStateHandle[WrittenReviewsFragment.KEY_USER_ID]
        ?: throw IllegalArgumentException("작성한 리뷰 화면으로 이동할 때 유저 아이디 안넘김")

    val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = loginUserId.flatMapLatest {
        if (it == null) flowOf(null) else userRepository.getUser(userId = it)
    }.map { user ->
        when (user) {
            is JoinedUser -> user
            LeavedUser, null -> null
        }
    }.viewModelStateIn(initialValue = null)

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val reviews: StateFlow<List<ReviewUiState>> = combine(
        loginUser,
        userRepository.getUser(authorId).filterIsInstance<JoinedUser>(),
        reviewRepository.getReviewsOfUser(authorId),
    ) { loginUser, author, reviews ->
        val facilityIds = reviews.map { it.facilityId }
        val facilities = facilityRepository.getFacilities(facilityIds)
        reviews.toReviewUiStates(loginUser, author, facilities)
    }.onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = emptyList())

    private suspend fun FacilityRepository.getFacilities(facilityIds: List<Long>): Map<Long, Facility> {
        if (facilityIds.isEmpty()) return emptyMap()
        return getFacilities(
            page = FIRST_PAGE,
            size = MAX_FACILITY_SIZE,
            conditions = AllFacilityFilterConditions(ids = facilityIds.toSet())
        ).associateBy { it.id }
    }

    private fun List<Review>.toReviewUiStates(
        loginUser: JoinedUser?,
        author: JoinedUser,
        facilities: Map<Long, Facility>,
    ): List<ReviewUiState> = mapNotNull { review ->
        val facility = facilities[review.facilityId] ?: return@mapNotNull null
        ReviewUiState(
            author = author,
            isDeletable = loginUser?.id == author.id,
            facility = facility,
            review = review,
        )
    }

    fun deleteReview(reviewId: String) = viewModelScope.launch {
        reviewRepository.deleteReview(reviewId)
    }

    companion object {

        private const val FIRST_PAGE = 0
        private const val MAX_FACILITY_SIZE = 10000
    }
}
