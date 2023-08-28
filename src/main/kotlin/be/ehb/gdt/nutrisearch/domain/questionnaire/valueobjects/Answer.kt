package be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects

import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Meal
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType

class Answer(
    val question: Question,
    val meal: Meal?,
    val answer: String? = null,
    @Field(targetType = FieldType.OBJECT_ID)
    @JsonIgnore
    val studyId: String? = null,
    @Id
    val id: String = ObjectId().toHexString()
) : Comparable<Answer> {

    override fun compareTo(other: Answer): Int {
        val defaultValue = question.id.compareTo(other.question.id)
        return if (defaultValue == 0) {
            if (meal == null || other.meal == null) 0 else meal.compareTo(other.meal)
        } else {
            defaultValue
        }
    }

    class Question(val question: String, val id: String, val options: List<String>?) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Question

            return id == other.id
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }

}