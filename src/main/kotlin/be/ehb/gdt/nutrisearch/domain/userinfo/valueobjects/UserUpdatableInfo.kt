package be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects

import java.time.LocalDate

class UserUpdatableInfo(
    val dob: LocalDate?,
    val activityLevel: ActivityLevel?,
    val length: Int?,
    val sex: Sex?,
    val isPregnant: Boolean?,
    val isBreastfeeding: Boolean?
)