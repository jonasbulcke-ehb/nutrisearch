package be.ehb.gdt.nutrisearch.domain.patients.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.repositories.ConsumptionRepository
import be.ehb.gdt.nutrisearch.domain.dietitians.repositories.DietitianRepository
import be.ehb.gdt.nutrisearch.domain.patients.valueobjects.Patient
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.UserInfoNotFoundException
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.NameRecord
import be.ehb.gdt.nutrisearch.excel.UserConsumptionsExcelWriter
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
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
    private val authFacade: AuthenticationFacade
) : PatientService {
    override fun getPatients(): List<NameRecord> {
        val dietitianId = dietitianRepo.findDietitianByAuthId(authFacade.authId)?.riziv ?: throw NoUserInfoForAuthenticationFound()
        val userInfoMap = userInfoRepo.findPatientsById(dietitianId).associateBy { it.authId }
        return firebaseAuth.getUsers(userInfoMap.keys.map { UidIdentifier(it) }).users
            .map { NameRecord(userInfoMap.getValue(it.uid).id, it.displayName) }
    }

    override fun getPatientInfo(id: String): Patient {
        return userInfoRepo.findUserInfoById(id)
            ?.let { Patient.from(firebaseAuth.getUser(it.authId).displayName, it) }
            ?: throw UserInfoNotFoundException(id)
    }

    override fun getConsumptions(id: String, timestamp: LocalDate): List<Consumption> =
        consumptionRepo.findConsumptionsByTimestampAndUserInfoId(timestamp, id)

    override fun exportConsumptionsToExcel(id: String, timestamp: LocalDate, outputStream: OutputStream) {
        consumptionRepo.findConsumptionsByTimestampAndUserInfoId(timestamp, id).also {
            UserConsumptionsExcelWriter(it, timestamp.toString()).write(outputStream)
        }
    }
}