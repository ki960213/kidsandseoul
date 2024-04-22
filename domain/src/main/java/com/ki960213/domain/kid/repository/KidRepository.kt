package com.ki960213.domain.kid.repository

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.kid.model.Kids
import com.ki960213.domain.user.model.Sex
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface KidRepository {

    fun getKids(userId: String): Flow<Kids>

    suspend fun addKid(
        parentId: String,
        name: String,
        sex: Sex,
        birthDate: LocalDate,
        livingDong: AdministrativeDong,
    )

    suspend fun deleteKid(parentId: String, kidId: String)
}
