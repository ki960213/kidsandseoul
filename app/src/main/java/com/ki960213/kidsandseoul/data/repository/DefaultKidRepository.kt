package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.kid.model.Kid
import com.ki960213.domain.kid.model.Kids
import com.ki960213.domain.kid.repository.KidRepository
import com.ki960213.domain.user.model.Sex
import com.ki960213.kidsandseoul.data.di.ApplicationScope
import com.ki960213.kidsandseoul.data.firebase.kid.KidDocument
import com.ki960213.kidsandseoul.data.firebase.kid.KidFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultKidRepository @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val administrativeDongRepository: AdministrativeDongRepository,
    private val kidFirestore: KidFirestore,
) : KidRepository {

    override fun getKids(userId: String): Flow<Kids> = combine(
        kidFirestore.getKids(userId),
        administrativeDongRepository.administrativeDongs,
    ) { kidDocuments, administrativeDongs ->
        val kids = kidDocuments.toKids(administrativeDongs)
        Kids(
            parentId = userId,
            kids = kids,
        )
    }

    private fun List<KidDocument>.toKids(
        administrativeDongs: Map<Long, AdministrativeDong>,
    ): List<Kid> = map { kidDocument -> kidDocument.toKid(administrativeDongs) }

    override suspend fun addKid(
        parentId: String,
        name: String,
        sex: Sex,
        birthDate: LocalDate,
        livingDong: AdministrativeDong,
    ) = externalScope.launch {
        kidFirestore.addKid(
            parentId = parentId,
            name = name,
            sex = sex,
            birthDate = birthDate,
            administrativeDongId = livingDong.id,
        )
    }.join()

    override suspend fun deleteKid(
        parentId: String,
        kidId: String,
    ) = externalScope.launch {
        kidFirestore.deleteKid(parentId, kidId)
    }.join()
}
