package be.ehb.gdt.nutrisearch.domain.questionnaire.repositories

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question
import java.time.LocalDate

interface QuestionRepository {
    fun findAllQuestionsByUserinfoId(id: String, date: LocalDate): List<Question>
    fun findAllQuestionsBySubject(subject: Question.Subject, requestDate: LocalDate): List<Question>
    fun saveQuestion(question: Question): Question
    fun deleteQuestionById(id: String)

}