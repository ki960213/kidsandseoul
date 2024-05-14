package com.ki960213.domain.facility.repository

import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.FacilityFilterConditions
import com.ki960213.domain.facility.model.FacilityOrder
import kotlinx.coroutines.flow.Flow

interface FacilityRepository {

    /**
     * 페이지는 0부터 시작
     */
    suspend fun getFacilities(
        page: Int,
        size: Int,
        order: FacilityOrder = FacilityOrder.None,
        conditions: FacilityFilterConditions,
    ): List<Facility>

    suspend fun getFacilitiesCount(conditions: FacilityFilterConditions): Int

    fun getInterestFacilities(userId: String): Flow<List<Facility>>

    /**
     * 유저의 관심 시설 추가하기
     *
     * 만약 이미 추가되어 있다면 아무 일도 안함
     * @param[facilityId] 추가할 시설 아이디
     * @param[userId] 유저 아이디
     */
    suspend fun addInterestFacility(facilityId: Long, userId: String)

    /**
     * 유저의 관심 시설 삭제하기
     *
     * 만약 이미 삭제되어 있다면 아무 일도 안함
     * @param[facilityId] 삭제할 시설 아이디
     * @param[userId] 유저 아이디
     */
    suspend fun deleteInterestFacility(facilityId: Long, userId: String)

    /**
     * 시설 단건 조회
     * @param[facilityId] 시설 아이디
     * @return [Facility]의 [Flow]
     */
    fun getFacility(facilityId: Long): Flow<Facility>
}
