package be.ehb.gdt.nutrisearch.domain.patients.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.repositories.ConsumptionRepository
import be.ehb.gdt.nutrisearch.domain.dietitians.repositories.DietitianRepository
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.patients.valueobjects.Patient
import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question
import be.ehb.gdt.nutrisearch.domain.questionnaire.services.QuestionService
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.UserInfoNotFoundException
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.NameRecord
import be.ehb.gdt.nutrisearch.excel.UserConsumptionsExcelWriter
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import be.ehb.gdt.nutrisearch.util.dietitianCheck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UidIdentifier
import org.springframework.stereotype.Service
import java.io.OutputStream
import java.time.LocalDate

@Service
class PatientServiceImpl(
    private val consumptionRepo: ConsumptionRepository,
    private val userInfoRepo: UserInfoRepository,
    private val dietitianRepo: DietitianRepository,
    private val firebaseAuth: FirebaseAuth,
    private val authFacade: AuthenticationFacade,
    private val questionService: QuestionService
) : PatientService {
    override fun getPatients(): List<NameRecord> {
        val dietitianId =
            dietitianRepo.findDietitianByAuthId(authFacade.authId)?.riziv ?: throw NoUserInfoForAuthenticationFound()
        val userInfoMap = userInfoRepo.findPatientsByDietitianId(dietitianId).associateBy { it.authId }
        return firebaseAuth.getUsers(userInfoMap.keys.map { UidIdentifier(it) }).users
            .map { NameRecord(userInfoMap.getValue(it.uid).id, it.displayName) }
    }

    override fun getPatientInfo(id: String): Patient {
        dietitianCheck(dietitianRepo.isTreatingPatient(authFacade.authId, id))
        return userInfoRepo.findUserInfoById(id)
            ?.let { Patient.from(firebaseAuth.getUser(it.authId).displayName, it) }
            ?: throw UserInfoNotFoundException(id)
    }

    override fun getConsumptions(id: String, timestamp: LocalDate): List<Consumption> {
        dietitianCheck(dietitianRepo.isTreatingPatient(authFacade.authId, id))
        return consumptionRepo.findConsumptionsByTimestampAndUserInfoId(timestamp, id)
    }

    override fun exportConsumptionsToExcel(id: String, timestamp: LocalDate, outputStream: OutputStream) {
        dietitianCheck(dietitianRepo.isTreatingPatient(authFacade.authId, id))
        consumptionRepo.findConsumptionsByTimestampAndUserInfoId(timestamp, id).also {
            UserConsumptionsExcelWriter(it, timestamp.toString()).write(outputStream)
        }
    }

    override fun getQuestions(id: String): List<Question> {
        dietitianCheck(dietitianRepo.isTreatingPatient(authFacade.authId, id))
        if(!userInfoRepo.existUserInfoById(id)) {
            throw ResourceNotFoundException("Userinfo", id)
        }
        return questionService.getQuestionsBySubject(Question.Subject.Type.USERINFO, id)
    }

    override fun createQuestion(id: String, question: Question): Question {
        dietitianCheck(dietitianRepo.isTreatingPatient(authFacade.authId, id))
        if (!userInfoRepo.existUserInfoById(id)) {
            throw ResourceNotFoundException("Userinfo", id);
        }
        return questionService.createQuestion(question, Question.Subject.Type.USERINFO, id)
    }
}