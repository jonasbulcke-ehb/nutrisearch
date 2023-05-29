package be.ehb.gdt.nutrisearch.domain.userinfo.entities

import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*

@Document("user-info")
class UserInfo(
    val dob: LocalDate,
    val activityLevel: ActivityLevel,
    val length: Int,
    val sex: Sex,
    val isPregnant: Boolean = false,
    val isBreastFeeding: Boolean = false,
    val weightMeasurementMeasurements: List<WeightMeasurement> = listOf(),
    @Id val id: String = ObjectId.get().toHexString()
) {
    @JsonIgnore
    @Indexed
    var authId: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserInfo

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}