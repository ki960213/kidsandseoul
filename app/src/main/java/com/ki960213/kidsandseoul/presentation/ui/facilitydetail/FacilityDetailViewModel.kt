package com.ki960213.kidsandseoul.presentation.ui.facilitydetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.facility.model.ChildCareFacility
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.KidsCafe
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.domain.review.repository.ReviewRepository
import com.ki960213.domain.user.model.JoinedUser
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class FacilityDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val facilityRepository: FacilityRepository,
    private val reviewRepository: ReviewRepository,
) : BaseViewModel() {

    private val facilityId: Long = savedStateHandle[FacilityDetailFragment.KEY_FACILITY_ID]
        ?: throw IllegalArgumentException("시설 상세 화면으로 이동할 때 시설 아이디 안넘김")

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUser: StateFlow<JoinedUser?> = authRepository.loginUserId
        .filterNotNull()
        .flatMapLatest { userRepository.getUser(it) }
        .filterIsInstance<JoinedUser>()
        .viewModelStateIn(initialValue = null)

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val facility: StateFlow<Facility?> = facilityRepository.getFacility(facilityId)
        .onEach { _isLoading.value = false }
        .viewModelStateIn(initialValue = null)

    val isSaturdayOperate: StateFlow<Boolean> = facility.filterIsInstance<ChildCareFacility>()
        .map { it.isSaturdayOperate }
        .viewModelStateIn(initialValue = false)

    val operateDays: StateFlow<Set<DayOfWeek>> = facility.filterIsInstance<KidsCafe>()
        .map { it.operatingDays.toSet() }
        .viewModelStateIn(initialValue = emptySet())

    val reviews: StateFlow<List<ReviewUiState>> = combine(
        facility.filterNotNull(),
        reviewRepository.getReviews(facilityId)
    ) { facility, reviews ->
        val authorIds = reviews.map { it.authorId }.distinct()
        val authors = userRepository.getUsers(authorIds)
            .filterIsInstance<JoinedUser>()
            .associateBy { it.id }
        reviews.mapNotNull { review ->
            val author = authors[review.authorId] ?: return@mapNotNull null
            ReviewUiState(author, loginUser.value?.id == author.id, facility, review)
        }
    }.viewModelStateIn(initialValue = emptyList())

    fun changeInterestFacility(willAdd: Boolean) = viewModelScope.launch {
        val facilityId = facility.value?.id ?: return@launch
        val loginUser = loginUser.value ?: return@launch
        if (willAdd) {
            facilityRepository.addInterestFacility(facilityId, loginUser.id)
        } else {
            facilityRepository.deleteInterestFacility(facilityId, loginUser.id)
        }
    }

    fun deleteReview(reviewId: String) = viewModelScope.launch {
        reviewRepository.deleteReview(reviewId)
    }
}
