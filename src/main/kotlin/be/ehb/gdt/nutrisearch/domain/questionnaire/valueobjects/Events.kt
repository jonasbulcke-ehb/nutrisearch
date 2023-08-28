package be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question

data class QuestionCreatedEvent(val question: Question)
data class QuestionDeletedEvent(val questionId: String)