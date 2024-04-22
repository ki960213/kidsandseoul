package com.ki960213.domain.administrativedong.repository

import com.ki960213.domain.administrativedong.model.AdministrativeDong

interface AdministrativeDongRepository {

    /**
     * 모든 행정동 조회
     * @return [AdministrativeDong] 목록
     */
    suspend fun getAdministrativeDongs(): Map<Long, AdministrativeDong>
}
