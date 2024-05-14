package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.network.administrativedong.AdministrativeDongApi
import com.ki960213.kidsandseoul.data.network.administrativedong.model.NetworkAdministrativeDong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAdministrativeDongRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val api: AdministrativeDongApi,
) : AdministrativeDongRepository {

    override val administrativeDongs: Flow<Map<Long, AdministrativeDong>> = flow {
        val administrativeDongs = api.getAdministrativeDongs()
            .map(NetworkAdministrativeDong::asAdministrativeDong)
            .sortedWith(compareBy({ it.borough.name }, { it.name }))
            .associateBy { it.id }
        emit(administrativeDongs)
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyMap()
    ).filter { it.isNotEmpty() }
}
