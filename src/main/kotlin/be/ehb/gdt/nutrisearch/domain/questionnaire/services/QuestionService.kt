package be.ehb.gdt.nutrisearch.domain.questionnaire.services

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question

interface QuestionService {
    fun getQuestionsBySubject(subject: Question.Subject): List<Question>
    fun getQuestionsBySubject(subjectType: Question.Subject.Type, id: String) =
        getQuestionsBySubject(Question.Subject(subjectType, id))

    fun createQuestion(question: Question, subject: Question.Subject): Question
    fun createQuestion(question: Question, subjectType: Question.Subject.Type, id: String) =
        createQuestion(question, Question.Subject(subjectType, id))

    fun deleteQuestionBySubject(type: Question.Subject.Type, id: String, questionId: String) =
        deleteQuestionBySubject(Question.Subject(type, id), questionId)

    fun deleteQuestionBySubject(subject: Question.Subject, questionId: String)
}
