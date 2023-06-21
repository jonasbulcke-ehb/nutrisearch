package be.ehb.gdt.nutrisearch.domain.study.services

import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy

interface StudyService {
    fun getStudies(): List<Study>
    fun getStudy(id: String): Study
    fun createStudy(study: Study): Study
    fun updateStudy(id: String, study: UpdatableStudy)
    fun terminateStudy(id: String)
    fun deleteStudy(id: String)
    fun joinStudy(id: String)
    fun leaveStudy(id: String)
}