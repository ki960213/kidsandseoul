package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.FacilityFilterConditions
import com.ki960213.domain.facility.model.FacilityOrder
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.user.UserFirestore
import com.ki960213.kidsandseoul.data.network.facility.FacilityApi
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkAllFilterConditions
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkFacility
import com.ki960213.kidsandseoul.data.network.facility.model.NetworkFacilityFilterConditions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultFacilityRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val api: FacilityApi,
    private val userFirestore: UserFirestore,
) : FacilityRepository {

    override suspend fun getFacilities(
        page: Int,
        size: Int,
        order: FacilityOrder,
        conditions: FacilityFilterConditions,
    ): List<Facility> = api.getFacilities(
        page = page,
        size = size,
        sort = FacilityApi.createSortParam(order),
        filterConditions = NetworkFacilityFilterConditions.from(conditions)
    ).map(NetworkFacility::asFacility)

    override suspend fun getFacilitiesCount(
        conditions: FacilityFilterConditions,
    ): Int = api.getFacilityCount(NetworkFacilityFilterConditions.from(conditions)).toInt()

    override fun getInterestFacilities(userId: String): Flow<List<Facility>> =
        userFirestore.getInterestFacilityIds(userId)
            .map { ids ->
                if (ids.isEmpty()) return@map emptyList()
                val networkFacilities = api.getFacilities(
                    filterConditions = NetworkAllFilterConditions(ids = ids.toSet())
                )
                networkFacilities.map(NetworkFacility::asFacility)
            }

    override suspend fun addInterestFacility(
        facilityId: Long,
        userId: String,
    ) = externalScope.launch {
        userFirestore.addInterestFacilityId(userId, facilityId)
    }.join()

    override suspend fun deleteInterestFacility(
        facilityId: Long,
        userId: String,
    ) = externalScope.launch {
        userFirestore.deleteInterestFacilityId(userId, facilityId)
    }.join()

    override fun getFacility(facilityId: Long): Flow<Facility> =
        flow { emit(api.getFacility(facilityId).asFacility()) }
}
