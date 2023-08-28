package be.ehb.gdt.nutrisearch.domain.questionnaire.entities

import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.Answer
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("questionnaires")
class Questionnaire(
    val answers: List<Answer>,
    @Id
    val id: Identifier
) {
    constructor(answers: List<Answer>, date: LocalDate, userinfoId: String)
            : this(answers, Identifier(date, userinfoId))

    data class Identifier(val date: LocalDate, val userinfoId: String) {
        constructor(userinfoId: String) : this(LocalDate.now(), userinfoId)
    }
}