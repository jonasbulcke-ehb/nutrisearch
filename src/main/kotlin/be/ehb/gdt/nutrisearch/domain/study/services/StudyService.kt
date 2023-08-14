package be.ehb.gdt.nutrisearch.domain.study.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import java.io.OutputStream
import java.time.LocalDate

interface StudyService {
    fun getStudies(): List<Study>
    fun getStudy(id: String): Study
    fun createStudy(study: Study): Study
    fun updateStudy(id: String, study: UpdatableStudy)
    fun terminateStudy(id: String)
    fun deleteStudy(id: String)
    fun joinStudy(id: String)
    fun leaveStudy(id: String)
    fun exportToExcel(id: String, timestamp: LocalDate, outputStream: OutputStream)
    fun getConsumptions(id: String, timestamp: LocalDate): List<Consumption>
    fun getParticipants(id: String): List<UserInfo>
}