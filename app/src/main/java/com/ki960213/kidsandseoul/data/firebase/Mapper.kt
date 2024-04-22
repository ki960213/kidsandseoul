package com.ki960213.kidsandseoul.data.firebase

import com.ki960213.domain.user.model.Sex

fun String.asSex(): Sex = when (this) {
    "MALE" -> Sex.MALE
    "FEMALE" -> Sex.FEMALE
    else -> throw IllegalStateException("파이어스토어에 성별 속성의 값이 올바르지 않습니다.")
}
