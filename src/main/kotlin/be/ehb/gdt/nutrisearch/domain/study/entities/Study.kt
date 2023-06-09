package be.ehb.gdt.nutrisearch.domain.study.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("studies")
class Study(
    val subject: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val participants: List<String> = listOf(),
    @JsonIgnore
    private val isDeleted: Boolean = false,
    @Id
    val id: String = ObjectId().toHexString()
) {
    val isActive: Boolean
        get() = endDate?.isAfter(LocalDate.now()) ?: true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Study

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}