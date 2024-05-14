package com.ki960213.domain.administrativedong.repository

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import kotlinx.coroutines.flow.Flow

interface AdministrativeDongRepository {

    val administrativeDongs: Flow<Map<Long, AdministrativeDong>>
}
