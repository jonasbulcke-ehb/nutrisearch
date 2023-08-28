package be.ehb.gdt.nutrisearch.domain.questionnaire.services

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Questionnaire
import java.time.LocalDate
 interface QuestionnaireService {
    fun getQuestionnaire(userinfoId: String, date: LocalDate): Questionnaire
    fun addAnswerToQuestionnaire(date: LocalDate, userinfoId: String, answerId: String, answer: String)
}