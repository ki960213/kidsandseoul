package com.ki960213.kidsandseoul.data.firebase

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

fun Timestamp.toLocalDate(): LocalDate = toDate().toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

fun LocalDate.toTimestamp(): Timestamp {
    val zoneId = ZoneId.systemDefault()
    val instant = atStartOfDay(zoneId).toInstant()
    val date = Date.from(instant)
    return Timestamp(date)
}

fun Timestamp.toLocalDateTime(): LocalDateTime = toDate().toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()

fun LocalDateTime.toTimestamp(): Timestamp {
    val zoneId = ZoneId.systemDefault()
    val instant = atZone(zoneId).toInstant()
    val date = Date.from(instant)
    return Timestamp(date)
}
