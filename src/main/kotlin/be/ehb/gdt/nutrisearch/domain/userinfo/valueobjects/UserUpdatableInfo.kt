package be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.ActivityLevel
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.Sex
import java.time.LocalDateTime

class UserUpdatableInfo(
    val dob: LocalDateTime?,
    val activityLevel: ActivityLevel?,
    val length: Int?,
    val sex: Sex?,
    val isPregnant: Boolean?,
    val isBreastFeeding: Boolean?
)