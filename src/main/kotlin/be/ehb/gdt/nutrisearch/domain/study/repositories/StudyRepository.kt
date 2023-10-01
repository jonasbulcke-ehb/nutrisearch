package be.ehb.gdt.nutrisearch.domain.study.repositories

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.Answer
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import java.time.LocalDate

interface StudyRepository {
    fun findAllStudies(): List<Study>
    fun findStudy(id: String): Study?
    fun findParticipantsIdsById(id: String): List<String>?
    fun findParticipatingStudy(authId: String): Study?
    fun insertStudy(study: Study): Study
    fun updateStudy(study: UpdatableStudy)
    fun updateEndDate(id: String, endDate: LocalDate)
    fun addParticipant(studyId: String, userInfoId: String)
    fun deleteParticipant(studyId: String, userInfoId: String)
    fun deleteParticipantInAllStudies(userInfoId: String)
    fun deleteStudy(id: String)
    fun hardDeleteStudy(id: String)
    fun existsById(id: String): Boolean
    fun findConsumptionsByStudyId(id: String, timestamp: LocalDate): List<Consumption>
    fun findAnswersByStudyId(id: String, date: LocalDate): Map<String, List<Answer>>
}