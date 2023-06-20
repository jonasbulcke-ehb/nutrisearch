package be.ehb.gdt.nutrisearch.domain.study.services

import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.repositories.StudyRepository
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.stereotype.Service
import java.time.LocalDate
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.Study as StudyValueObject

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
        if (userInfo.currentStudy?.isActive == true) {
            throw RuntimeException("User cannot participate in more then one study at the same time")
        }
        val study = studyRepo.findStudy(id) ?: throw ResourceNotFoundException(Study::class.java, id)
        userInfoRepo.updateCurrentStudy(
            userInfo.id,
            study.let { StudyValueObject(it.id, it.subject, it.startDate, it.endDate) }
        )
        studyRepo.addParticipant(id, userInfo.id)
    }

    override fun leaveStudy(id: String) {
        val userInfoId = userInfoRepo.findUserInfoIdByAuthId(authFacade.authentication.name)
            ?: throw NoUserInfoForAuthenticationFound()
        if (!studyRepo.existsById(id)) {
            throw ResourceNotFoundException(Study::class.java, id)
        }
        userInfoRepo.clearCurrentStudy(userInfoId)
        studyRepo.deleteParticipant(id, userInfoId)
    }

    override fun terminateStudy(id: String) {
        if (!studyRepo.existsById(id)) {
            throw ResourceNotFoundException(Study::class.java, id)
        }
        studyRepo.updateEndDate(id, LocalDate.now());
    }

    override fun deleteStudy(id: String) {
        studyRepo.deleteStudy(id)
    }
}
