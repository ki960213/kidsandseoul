package com.ki960213.domain.kid.model

import com.ki960213.domain.administrativedong.model.AdministrativeDong
import com.ki960213.domain.user.model.Sex
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Kid(
    val id: String,
    val name: String,
    val sex: Sex,
    val birthDate: LocalDate,
    val livingDong: AdministrativeDong,
) {

    val age: Int = ChronoUnit.YEARS.between(birthDate, LocalDate.now()).toInt() + 1
}
