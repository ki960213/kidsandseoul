package com.ki960213.kidsandseoul.data.database

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {

    @TypeConverter
    fun convertToDateTime(dateTimeStr: String): LocalDateTime = LocalDateTime.parse(dateTimeStr)

    @TypeConverter
    fun convertToDateTimeStr(dateTime: LocalDateTime): String = dateTime.toString()
}
