package be.ehb.gdt.nutrisearch.domain.questionnaire.services

import be.ehb.gdt.nutrisearch.domain.dietitians.repositories.DietitianRepository
import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question
import be.ehb.gdt.nutrisearch.domain.questionnaire.repositories.QuestionRepository
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.QuestionCreatedEvent
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.QuestionDeletedEvent
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import be.ehb.gdt.nutrisearch.util.dietitianCheck
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class QuestionServiceImpl(
    private val authFacade: AuthenticationFacade,
    private val dietitianRepo: DietitianRepository,
    private val questionRepo: QuestionRepository,
    private val publisher: ApplicationEventPublisher
) : QuestionService {
    override fun getQuestionsBySubject(subject: Question.Subject): List<Question> {
        if (subject.type == Question.Subject.Type.USERINFO) {
            dietitianCheck(dietitianRepo.isTreatingPatient(authFacade.authId, subject.id))
        }
        return questionRepo.findAllQuestionsBySubject(subject, LocalDate.now())
    }

    override fun createQuestion(question: Question, subject: Question.Subject): Question {
        if(subject.type == Question.Subject.Type.USERINFO) {
            dietitianCheck(dietitianRepo.isTreatingPatient(authFacade.authId, subject.id))
        }
        check(question.type == Question.Type.SHORT_ANSWER || question.options != null) { "Question needs to have options when it's not a short answer type" }
        check(question.type != Question.Type.SHORT_ANSWER || question.options?.isNotEmpty() != true) { "Question cannot have options when it's a short answer type" }
        return question.apply {
            this.subject = subject
        }.also {
            questionRepo.saveQuestion(it)
            publisher.publishEvent(QuestionCreatedEvent(it))
        }
    }

    override fun deleteQuestionBySubject(subject: Question.Subject, questionId: String) {
        if(subject.type == Question.Subject.Type.USERINFO) {
            dietitianCheck(dietitianRepo.isTreatingPatient(authFacade.authId, subject.id))
        }
        questionRepo.deleteQuestionById(questionId)
        publisher.publishEvent(QuestionDeletedEvent(questionId))
    }


}