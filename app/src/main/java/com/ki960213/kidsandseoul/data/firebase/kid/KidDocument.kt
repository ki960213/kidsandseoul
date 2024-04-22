package com.ki960213.kidsandseoul.data.firebase.kid

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.kid.model.Kid
import com.ki960213.kidsandseoul.data.firebase.asSex
import com.ki960213.kidsandseoul.data.firebase.toLocalDate
import com.ki960213.kidsandseoul.data.firebase.toTimestamp

@Keep
data class KidDocument(
    @JvmField @PropertyName(FIELD_ID) val id: String = "",
    @JvmField @PropertyName(FIELD_NAME) val name: String = "",
    @JvmField @PropertyName(FIELD_SEX) val sex: String = "",
    @JvmField @PropertyName(FIELD_ADMINISTRATIVE_DONG_ID) val administrativeDongId: Long = -1,
    @JvmField @PropertyName(FIELD_BIRTH_DATE) val birthDate: Timestamp = Timestamp.now(),
) {

    constructor(kid: Kid) : this(
        id = kid.id,
        name = kid.name,
        sex = kid.sex.toString(),
        administrativeDongId = kid.livingDong.id,
        birthDate = kid.birthDate.toTimestamp(),
    )

    fun toKid(administrativeDongs: Map<Long, AdministrativeDong>) = Kid(
        id = id,
        name = name,
        sex = sex.asSex(),
        livingDong = administrativeDongs[administrativeDongId]
            ?: throw AssertionError("행정동 데이터 잘못 초기화한듯"),
        birthDate = birthDate.toLocalDate(),
    )

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_SEX = "sex"
        const val FIELD_ADMINISTRATIVE_DONG_ID = "administrativeDongId"
        const val FIELD_BIRTH_DATE = "birthDate"
    }
}
