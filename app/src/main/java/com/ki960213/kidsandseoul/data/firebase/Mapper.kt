package com.ki960213.kidsandseoul.data.firebase

import com.ki960213.domain.user.model.Sex
import dev.gitlive.firebase.firestore.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun String.asSex(): Sex = when (this) {
    "MALE" -> Sex.MALE
    "FEMALE" -> Sex.FEMALE
    else -> throw IllegalStateException("파이어스토어에 성별 속성의 값이 올바르지 않습니다.")
}

fun Timestamp.toLocalDate(): LocalDate =
    Instant.ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

fun LocalDate.toTimestamp(): Timestamp = atStartOfDay()
    .let { dateTime ->
        val epochSecond = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
        Timestamp(epochSecond, dateTime.nano)
    }

fun Timestamp.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
