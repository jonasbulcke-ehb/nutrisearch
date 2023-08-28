package be.ehb.gdt.nutrisearch.domain.questionnaire.services

import be.ehb.gdt.nutrisearch.domain.exceptions.NoQuestionsAvailable
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question
import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Questionnaire
import be.ehb.gdt.nutrisearch.domain.questionnaire.repositories.QuestionRepository
import be.ehb.gdt.nutrisearch.domain.questionnaire.repositories.QuestionnaireRepository
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.QuestionCreatedEvent
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.QuestionDeletedEvent
import be.ehb.gdt.nutrisearch.domain.study.repositories.StudyRepository
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.StudyJoinedEvent
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.StudyLeftEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class QuestionnaireServiceImpl(
    private val repo: QuestionnaireRepository,
    private val questionRepo: QuestionRepository,
    private val studyRepo: StudyRepository
) : QuestionnaireService {
    override fun getQuestionnaire(userinfoId: String, date: LocalDate): Questionnaire {
        if (date.isAfter(LocalDate.now())) {
            throw NoQuestionsAvailable(userinfoId, date)
        }
        return repo.findQuestionnaireById(Questionnaire.Identifier(date, userinfoId))
            ?: questionRepo.findAllQuestionsByUserinfoId(userinfoId, date)
                .ifEmpty { throw NoQuestionsAvailable(userinfoId, date) }.flatMap { it.toAnswers() }
                .let { Questionnaire(it, date, userinfoId) }.also { repo.saveQuestionnaire(it) }
    }

    override fun addAnswerToQuestionnaire(date: LocalDate, userinfoId: String, answerId: String, answer: String) {
        repo.updateAnswer(Questionnaire.Identifier(date, userinfoId), answerId, answer)
    }

    @EventListener(QuestionCreatedEvent::class)
    fun handleQuestionCreatedEvent(event: QuestionCreatedEvent) {
        if (event.question.subject.type == Question.Subject.Type.USERINFO) {
            event.question.toAnswers().also {
                repo.insertAnswersToQuestionnaire(Questionnaire.Identifier(event.question.subject.id), it)
            }
        } else {
            val answers = event.question.toAnswers()
            val ids = studyRepo.findParticipantsIdsById(event.question.subject.id)
                ?.map { Questionnaire.Identifier(it) } ?: throw ResourceNotFoundException(
                "Study",
                event.question.subject.id
            )
            repo.insertAnswersToQuestionnaires(ids, answers)
        }
    }

    @EventListener(StudyJoinedEvent::class)
    fun handleStudyJoinedEvent(event: StudyJoinedEvent) {
        questionRepo.findAllQuestionsBySubject(Question.Subject(Question.Subject.Type.STUDY, event.studyId), LocalDate.now())
            .flatMap { it.toAnswers() }
            .also { repo.insertAnswersToQuestionnaire(Questionnaire.Identifier(event.userinfoId), it) }
    }

    @EventListener(StudyLeftEvent::class)
    fun handleStudyLeftEvent(event: StudyLeftEvent) = repo.deleteAnswersByUserinfoIdAndStudyId(event.userinfoId, event.studyId)

    @EventListener(QuestionDeletedEvent::class)
    fun handleQuestionDeletedEvent(event: QuestionDeletedEvent) {
        repo.deleteAnswersByQuestionId(event.questionId)
    }
}