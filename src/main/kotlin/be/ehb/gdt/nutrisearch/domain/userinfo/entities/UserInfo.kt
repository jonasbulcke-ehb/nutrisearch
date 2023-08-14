package be.ehb.gdt.nutrisearch.domain.userinfo.entities

import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.ActivityLevel
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.Sex
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.LocalDate
import java.util.*

@Document("userinfo")
class UserInfo(
    @Field(targetType = FieldType.STRING)
    val dob: LocalDate,
    val activityLevel: ActivityLevel,
    val length: Int,
    val sex: Sex,
    val isPregnant: Boolean = false,
    val isBreastfeeding: Boolean = false,
    val weightMeasurements: List<WeightMeasurement> = listOf(),
    @JsonIgnore
    val treatmentTeam: List<String> = listOf(),
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