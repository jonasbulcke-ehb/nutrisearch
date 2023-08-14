package be.ehb.gdt.nutrisearch.domain.dietitians.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("dietitians")
class Dietitian(
    @Id
    val riziv: String,
    val firstname: String,
    val lastname: String,
    @JsonIgnore
    @Indexed(unique = true)
    val authId: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val dietitian = other as Dietitian
        return riziv == dietitian.riziv
    }

    override fun hashCode() = riziv.hashCode()
}
