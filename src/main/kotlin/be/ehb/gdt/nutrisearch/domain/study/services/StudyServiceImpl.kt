package be.ehb.gdt.nutrisearch.domain.study.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.exceptions.AlreadyParticipatingInActiveStudyException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.repositories.StudyRepository
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.excel.StudyConsumptionsExcelWriter
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.stereotype.Service
import java.io.OutputStream
import java.time.LocalDate

@Service
class StudyServiceImpl(
    private val studyRepo: StudyRepository,
    private val authFacade: AuthenticationFacade,
    private val userInfoRepo: UserInfoRepository,
) :
    StudyService {
    override fun getStudies(): List<Study> {
        return studyRepo.findAllStudies()
    }

    override fun getStudy(id: String): Study {
        return studyRepo.findStudy(id) ?: throw ResourceNotFoundException(Study::class.java, id)
    }

    override fun createStudy(study: Study): Study {
        return studyRepo.insertStudy(study)
    }

    override fun updateStudy(id: String, study: UpdatableStudy) {
        if (id != study.id) {
            throw ResourceDoesNotMatchIdException(study.id, id)
        }
        if (!studyRepo.existsById(id)) {
            throw ResourceNotFoundException(Study::class.java, id)
        }
        studyRepo.updateStudy(study)
    }

    override fun joinStudy(id: String) {
        val userInfo = userInfoRepo.findUserInfoByAuthId(authFacade.authentication.name)
            ?: throw NoUserInfoForAuthenticationFound()
        val currentStudy = userInfoRepo.findCurrentStudyById(id)
        if (currentStudy?.isActive == true) {
            throw AlreadyParticipatingInActiveStudyException(userInfo.id, currentStudy.id)
        }
        studyRepo.addParticipant(id, userInfo.id)
    }

    override fun leaveStudy(id: String) {
        val userInfoId = userInfoRepo.findUserInfoIdByAuthId(authFacade.authentication.name)
            ?: throw NoUserInfoForAuthenticationFound()
        if (!studyRepo.existsById(id)) {
            throw ResourceNotFoundException(Study::class.java, id)
        }
        studyRepo.deleteParticipant(id, userInfoId)
    }

    override fun terminateStudy(id: String) {
        if (!studyRepo.existsById(id)) {
            throw ResourceNotFoundException(Study::class.java, id)
        }
        studyRepo.updateEndDate(id, LocalDate.now())
    }

    override fun deleteStudy(id: String) {
        studyRepo.deleteStudy(id)
    }

    override fun exportToExcel(id: String, timestamp: LocalDate, outputStream: OutputStream) {
        if (!studyRepo.existsById(id)) {
            throw ResourceNotFoundException(Study::class.java, id)
        }
        val consumptions = studyRepo.findConsumptionsByStudyId(id, timestamp).groupBy { it.userInfoId }
        studyRepo.findStudy(id)!!.participants.associateWith { consumptions.getOrDefault(it.id, listOf()) }.also {
            StudyConsumptionsExcelWriter(it, timestamp.toString()).write(outputStream)
        }
    }

    override fun getConsumptions(id: String, timestamp: LocalDate): List<Consumption> {
        return studyRepo.findConsumptionsByStudyId(id, timestamp)
    }

    override fun getParticipants(id: String): List<UserInfo> =
        studyRepo.findStudy(id)?.participants ?: throw ResourceNotFoundException("Study", id)
}
