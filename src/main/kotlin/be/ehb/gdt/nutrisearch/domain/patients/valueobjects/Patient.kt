package be.ehb.gdt.nutrisearch.domain.patients.valueobjects

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.ActivityLevel
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.Sex
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import java.time.LocalDate

class Patient(
    val id: String,
    val name: String,
    val dob: LocalDate,
    val activityLevel: ActivityLevel,
    val length: Int,
    val sex: Sex,
    val isPregnant: Boolean,
    val isBreastfeeding: Boolean,
    val weightMeasurements: List<WeightMeasurement>
) {

    companion object {
        fun from(name: String, userInfo: UserInfo): Patient {
            return Patient(
                userInfo.id,
                name,
                userInfo.dob,
                userInfo.activityLevel,
                userInfo.length,
                userInfo.sex,
                userInfo.isPregnant,
                userInfo.isBreastfeeding,
                userInfo.weightMeasurements
            )
        }
    }
}
