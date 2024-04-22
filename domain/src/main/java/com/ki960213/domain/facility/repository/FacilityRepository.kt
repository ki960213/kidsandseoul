package com.ki960213.domain.facility.repository

import com.ki960213.domain.facility.model.Facilities
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.FacilityFilterCondition
import kotlinx.coroutines.flow.Flow

interface FacilityRepository {

    /**
     * 필터링된 시설 목록 조회
     * @param[condition] 시설 필터 조건
     * @return [Facilities]의 [Flow]
     */
    suspend fun getFacilities(condition: FacilityFilterCondition): Facilities

    /**
     * 아이디로 필터링된 시설 목록 조회
     * @param[facilityIds] 시설 아이디 목록
     * @return [Facility] 목록
     */
    suspend fun getFacilities(facilityIds: List<Long>): Facilities

    /**
     * 유저가 관심 있어 하는 시설 목록 조회
     * @param[userId] 유저 아이디
     * @return [Facilities]의 [Flow]
     */
    fun getInterestFacilities(userId: String): Flow<Facilities>

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
     * 최근 핫한 시설 목록 조회
     * @return [Facilities]의 [Flow]
     */
    fun getHotFacilities(): Flow<Facilities>

    /**
     * 시설 단건 조회
     * @param[facilityId] 시설 아이디
     * @return [Facility]의 [Flow]
     */
    fun getFacility(facilityId: Long): Flow<Facility>
}
