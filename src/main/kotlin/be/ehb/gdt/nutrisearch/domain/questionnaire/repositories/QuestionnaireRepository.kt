package be.ehb.gdt.nutrisearch.domain.questionnaire.repositories

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Questionnaire
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.Answer

interface QuestionnaireRepository {
    fun findQuestionnaireById(id: Questionnaire.Identifier): Questionnaire?
    fun saveQuestionnaire(questionnaire: Questionnaire): Questionnaire
    fun insertAnswersToQuestionnaire(id: Questionnaire.Identifier, answers: List<Answer>)
    fun insertAnswersToQuestionnaires(ids: List<Questionnaire.Identifier>, answers: List<Answer>)
    fun updateAnswer(id: Questionnaire.Identifier, answerId: String, answer: String)
    fun deleteAnswersByUserinfoIdAndStudyId(userinfoId: String, studyId: String)
    fun deleteAnswersByQuestionId(questionId: String)
}