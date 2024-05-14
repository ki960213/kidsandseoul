package com.ki960213.kidsandseoul.data.firebase.kid

import androidx.annotation.Keep
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.kid.model.Kid
import com.ki960213.kidsandseoul.data.firebase.Document
import com.ki960213.kidsandseoul.data.firebase.asSex
import com.ki960213.kidsandseoul.data.firebase.toLocalDate
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class KidDocument(
    val id: String = "",
    val name: String = "",
    val sex: String = "",
    val administrativeDongId: Long = -1,
    val birthDate: Timestamp = Timestamp.now(),
) : Document {

    override fun isValid(): Boolean = id.isNotBlank()

    fun toKid(administrativeDongs: Map<Long, AdministrativeDong>) = Kid(
        id = id,
        name = name,
        sex = sex.asSex(),
        livingDong = administrativeDongs[administrativeDongId]
            ?: throw AssertionError("행정동 데이터 잘못 초기화한듯"),
        birthDate = birthDate.toLocalDate(),
    )
}
