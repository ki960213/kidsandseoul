package com.ki960213.kidsandseoul.presentation.ui.firstscreen.community.reviews

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val facilityRepository: FacilityRepository,
    private val reviewRepository: ReviewRepository,
) : BaseViewModel() {

    val loginUserId: StateFlow<String?> = authRepository.loginUserId
        .viewModelStateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = loginUserId.flatMapLatest { loginUserId ->
        if (loginUserId == null) flowOf(null) else userRepository.getUser(userId = loginUserId)
    }.map { user ->
        when (user) {
            is JoinedUser -> user
            LeavedUser, null -> null
        }
    }.viewModelStateIn(initialValue = null)

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val reviews: StateFlow<List<ReviewUiState>> = reviewRepository.getRecentReviews()
        .flatMapLatest { reviews ->
            combine(
                userRepository.getAuthorsOf(reviews),
                facilityRepository.getFacilitiesIn(reviews),
                loginUserId,
            ) { authors, facilities, loginUserId ->
                reviews.toReviewUiStates(authors, facilities, loginUserId)
            }
        }.onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = emptyList())

    private fun UserRepository.getAuthorsOf(reviews: List<Review>): Flow<Map<String, JoinedUser>> {
        val authorIds = reviews.map { it.authorId }.distinct()
        return getUsers(authorIds)
            .map { users ->
                users.filterIsInstance<JoinedUser>()
                    .associateBy { it.id }
            }
    }

    private suspend fun FacilityRepository.getFacilitiesIn(
        reviews: List<Review>,
    ): Flow<Map<Long, Facility>> {
        val facilityIds = reviews.map { it.facilityId }.distinct()
        if (facilityIds.isEmpty()) return flowOf(emptyMap())
        return flowOf(
            getFacilities(
                page = FIRST_PAGE,
                size = MAX_FACILITY_SIZE,
                conditions = AllFacilityFilterConditions(ids = facilityIds.toSet())
            ).associateBy { it.id }
        )
    }

    private fun List<Review>.toReviewUiStates(
        authors: Map<String, JoinedUser>,
        facilities: Map<Long, Facility>,
        loginUserId: String?,
    ): List<ReviewUiState> = mapNotNull { review ->
        val author = authors[review.authorId] ?: return@mapNotNull null
        val facility = facilities[review.facilityId] ?: return@mapNotNull null
        ReviewUiState(
            author = author,
            isDeletable = author.id == loginUserId,
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
