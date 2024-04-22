package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.facility.model.ChildCareFacilityFilterCondition
import com.ki960213.domain.facility.model.Facilities
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.FacilityFilterCondition
import com.ki960213.domain.facility.model.KidsCafeFilterCondition
import com.ki960213.domain.facility.model.OtherFacilityFilterCondition
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.facility.FacilityDocument
import com.ki960213.kidsandseoul.data.firebase.facility.FacilityFirestore
import com.ki960213.kidsandseoul.data.firebase.statistics.StatisticsFirestore
import com.ki960213.kidsandseoul.data.firebase.user.UserFirestore
import com.ki960213.kidsandseoul.data.network.facility.FacilityApi
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkChildCareFacility
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkFacility
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkKidsCafe
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkOtherFacility
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkService
import com.ki960213.kidsandseoul.data.network.facility.model.asNetworkService
import com.ki960213.kidsandseoul.data.network.facility.model.toFacility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultFacilityRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val api: FacilityApi,
    private val facilityFirestore: FacilityFirestore,
    private val userFirestore: UserFirestore,
    private val statisticsFirestore: StatisticsFirestore,
) : FacilityRepository {

    override suspend fun getFacilities(condition: FacilityFilterCondition): Facilities {
        val networkFacilities = when (condition) {
            is ChildCareFacilityFilterCondition -> api.getFacilities(
                name = condition.name?.takeIf { it.isNotBlank() },
                age = condition.age,
                administrativeDongId = condition.administrativeDong?.id,
                service = condition.childCareService?.asNetworkService(),
            )

            is KidsCafeFilterCondition -> api.getFacilities(
                name = condition.name?.takeIf { it.isNotBlank() },
                age = condition.age,
                administrativeDongId = condition.administrativeDong?.id,
                service = NetworkService.KIDS_CAFE,
            )

            is OtherFacilityFilterCondition -> api.getFacilities(
                name = condition.name?.takeIf { it.isNotBlank() },
                age = condition.age,
                administrativeDongId = condition.administrativeDong?.id,
                service = condition.facilityType?.asNetworkService(),
            )

            else -> api.getFacilities(
                name = condition.name?.takeIf { it.isNotBlank() },
                age = condition.age,
                administrativeDongId = condition.administrativeDong?.id,
            )
        }
        val facilityIds = networkFacilities.extractId()
        val facilityDocuments = facilityFirestore.fetchFacilityDocuments(facilityIds)
            .associateBy { it.id }
        return networkFacilities.toFacilities(facilityDocuments).apply(condition)
    }

    private fun List<NetworkFacility>.extractId(): List<Long> = map {
        when (it) {
            is NetworkChildCareFacility -> it.id
            is NetworkKidsCafe -> it.id
            is NetworkOtherFacility -> it.id
        }
    }

    override suspend fun getFacilities(facilityIds: List<Long>): Facilities {
        val networkFacilities = api.getFacilities(ids = facilityIds)
        val facilityDocuments =
            facilityFirestore.fetchFacilityDocuments(facilityIds).associateBy { it.id }
        return networkFacilities.toFacilities(facilityDocuments)
    }

    override fun getInterestFacilities(
        userId: String,
    ): Flow<Facilities> = facilityFirestore.getInterestFacilityIds(userId)
        .map { ids ->
            val networkFacilities = api.getFacilities(ids = ids)
            val facilityDocuments =
                facilityFirestore.fetchFacilityDocuments(ids).associateBy { it.id }
            networkFacilities.toFacilities(facilityDocuments)
        }

    private fun List<NetworkFacility>.toFacilities(
        facilityDocuments: Map<Long, FacilityDocument>,
    ): Facilities = map { networkFacility ->
        networkFacility.toFacility(
            reviewCount = facilityDocuments[networkFacility.id]?.reviewCount ?: 0,
            starPointAvg = facilityDocuments[networkFacility.id]?.starPointAvg ?: 0.0,
        )
    }.let { Facilities(allCount = size, value = it) }

    override suspend fun addInterestFacility(
        facilityId: Long,
        userId: String,
    ) = externalScope.launch {
        userFirestore.addInterestFacilityId(userId = userId, facilityId = facilityId)
        statisticsFirestore.plusHotPoint(facilityId = facilityId)
    }.join()

    override suspend fun deleteInterestFacility(
        facilityId: Long,
        userId: String,
    ) = externalScope.launch {
        userFirestore.deleteInterestFacilityId(userId = userId, facilityId = facilityId)
    }.join()

    override fun getHotFacilities(): Flow<Facilities> = flow {
        val facilityIds = statisticsFirestore.fetchHotFacilityIds()

        val networkFacilities = api.getFacilities(ids = facilityIds)
        val facilityDocuments =
            facilityFirestore.fetchFacilityDocuments(facilityIds).associateBy { it.id }
        emit(networkFacilities.toFacilities(facilityDocuments))
    }

    override fun getFacility(facilityId: Long): Flow<Facility> = combine(
        flow { emit(api.getFacility(facilityId)) },
        facilityFirestore.getFacility(facilityId),
    ) { networkFacility, facilityDocument ->
        networkFacility.toFacility(
            reviewCount = facilityDocument?.reviewCount ?: 0,
            starPointAvg = facilityDocument?.starPointAvg ?: 0.0,
        )
    }
}
