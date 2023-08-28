package be.ehb.gdt.nutrisearch.domain.questionnaire.entities

import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Meal
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.Answer
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.LocalDate

@Document("questions")
class Question(
    val question: String,
    val type: Type,
    val options: List<String>?,
    val frequency: Frequency,
    @Id
    val id: String = ObjectId().toHexString(),
    @JsonIgnore
    @Field(targetType = FieldType.INT64)
    val deletedAt: LocalDate?
) {
    @JsonIgnore
    lateinit var subject: Subject
    val isDeleted: Boolean
        get() = deletedAt != null

    fun toAnswers(): List<Answer> {
        val options =
            if (type == Type.SHORT_ANSWER) null else checkNotNull(options) { "Options cannot be null" }
        return if (frequency == Frequency.MEAL) {
            Meal.values().map { Answer(Answer.Question(question, id, options), it, studyId = subject.id) }
        } else {
            listOf(Answer(Answer.Question(question, id, options), null, studyId = subject.id))
        }
    }

    enum class Type {
        YES_NO,
        SHORT_ANSWER,
        MULTIPLE_CHOICE
    }

    enum class Frequency {
        DAY,
        MEAL
    }

    data class Subject(
        val type: Type,
        @Field(targetType = FieldType.OBJECT_ID)
        val id: String
    ) {

        enum class Type {
            USERINFO,
            STUDY
        }
    }
}