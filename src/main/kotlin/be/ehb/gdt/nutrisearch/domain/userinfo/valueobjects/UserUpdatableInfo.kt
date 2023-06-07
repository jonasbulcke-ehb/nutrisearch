package be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.ActivityLevel
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.Sex
import java.time.LocalDate

class UserUpdatableInfo(
    val dob: LocalDate?,
    val activityLevel: ActivityLevel?,
    val length: Int?,
    val sex: Sex?,
    val isPregnant: Boolean?,
    val isBreastFeeding: Boolean?
)