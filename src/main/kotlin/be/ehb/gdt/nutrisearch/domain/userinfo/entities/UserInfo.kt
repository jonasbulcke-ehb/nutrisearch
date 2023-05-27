package be.ehb.gdt.nutrisearch.domain.userinfo.entities

import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.Weight
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import java.time.LocalDateTime

class UserInfo(
    val dob: LocalDateTime,
    val activityLevel: ActivityLevel,
    val length: Int,
    val sex: Sex,
    val isPregnant: Boolean = false,
    val isBreastFeeding: Boolean = false,
    val measuredWeights: List<Weight> = listOf(),
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