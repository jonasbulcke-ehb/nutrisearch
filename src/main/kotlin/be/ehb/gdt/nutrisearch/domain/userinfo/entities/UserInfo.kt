package be.ehb.gdt.nutrisearch.domain.userinfo.entities

import org.bson.types.ObjectId
import java.time.LocalDateTime

class UserInfo(
    val sex: Sex,
    val dob: LocalDateTime,
    val length: Int,
    val weight: Int,
    val activityLevel: ActivityLevel,
    val isPregnant: Boolean = false,
    val isBreastFeeding: Boolean = false,
    val id: String = ObjectId.get().toHexString()
) {
    lateinit var authId: String
}