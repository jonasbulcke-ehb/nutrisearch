package be.ehb.gdt.nutrisearch.domain.study.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.exceptions.AlreadyParticipatingInActiveStudyException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question
import be.ehb.gdt.nutrisearch.domain.questionnaire.repositories.QuestionRepository
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.repositories.StudyRepository
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.StudyJoinedEvent
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.StudyLeftEvent
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserinfoDeletedEvent
import be.ehb.gdt.nutrisearch.excel.StudyConsumptionsExcelWriter
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.io.OutputStream
import java.time.LocalDate

@Service
class StudyServiceImpl(
    private val studyRepo: StudyRepository,
    private val authFacade: AuthenticationFacade,
    private val userInfoRepo: UserInfoRepository,
    private val publisher: ApplicationEventPublisher,
    private val questionRepo: QuestionRepository
) : StudyService {
    override fun getStudies(): List<Study> {
        return studyRepo.findAllStudies()
    }

    override fun getStudy(id: String): Study {
        return studyRepo.findStudy(id) ?: throw ResourceNotFoundException("Study", id)
    }

    override fun createStudy(study: Study): Study {
        return studyRepo.insertStudy(study)
    }

    override fun updateStudy(id: String, study: UpdatableStudy) {
        if (id != study.id) {
            throw ResourceDoesNotMatchIdException(study.id, id)
        }
        checkIfExists(id)
        studyRepo.updateStudy(study)
    }

    override fun joinStudy(id: String) {
        val userInfoId = userInfoRepo.findUserInfoIdByAuthId(authFacade.authentication.name)
            ?: throw NoUserInfoForAuthenticationFound()
        val currentStudy = userInfoRepo.findCurrentStudyById(id)
        if (currentStudy?.isActive == true) {
            throw AlreadyParticipatingInActiveStudyException(userInfoId, currentStudy.id)
        }
        studyRepo.addParticipant(id, userInfoId)
        publisher.publishEvent(StudyJoinedEvent(id, userInfoId))
    }

    override fun leaveStudy(id: String) {
        val userInfoId = userInfoRepo.findUserInfoIdByAuthId(authFacade.authentication.name)
            ?: throw NoUserInfoForAuthenticationFound()
        checkIfExists(id)
        studyRepo.deleteParticipant(id, userInfoId)
        publisher.publishEvent(StudyLeftEvent(id, userInfoId))
    }

    override fun terminateStudy(id: String) {
        checkIfExists(id)
        studyRepo.updateEndDate(id, LocalDate.now())
    }

    override fun deleteStudy(id: String) {
        studyRepo.deleteStudy(id)
    }

    override fun exportToExcel(id: String, timestamp: LocalDate, outputStream: OutputStream) {
        val study = studyRepo.findStudy(id) ?: throw ResourceNotFoundException("Study", id)
        val consumptions = studyRepo.findConsumptionsByStudyId(id, timestamp).groupBy { it.userInfoId }
        val answers = studyRepo.findAnswersByStudyId(id, timestamp)
            .ifEmpty {
                mapOf(
                    "PROTOTYPE" to questionRepo
                        .findAllQuestionsBySubject(Question.Subject(Question.Subject.Type.STUDY, id), timestamp)
                        .flatMap { it.toAnswers() })
            }
        study.participants.associateWith { consumptions.getOrDefault(it.id, listOf()) }.also {
            StudyConsumptionsExcelWriter(it, answers, timestamp).write(outputStream)
        }
    }

    override fun getConsumptions(id: String, timestamp: LocalDate): List<Consumption> {
        return studyRepo.findConsumptionsByStudyId(id, timestamp)
    }

    override fun getParticipants(id: String): List<UserInfo> =
        studyRepo.findStudy(id)?.participants ?: throw ResourceNotFoundException("Study", id)

    @EventListener(UserinfoDeletedEvent::class)
    fun handleUserinfoDeletedEvent(event: UserinfoDeletedEvent) {
        studyRepo.deleteParticipantInAllStudies(event.userinfoId)
    }

    private fun checkIfExists(id: String) {
        if (!studyRepo.existsById(id)) {
            throw ResourceNotFoundException("Study", id)
        }
    }
}
