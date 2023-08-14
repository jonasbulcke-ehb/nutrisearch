package be.ehb.gdt.nutrisearch.domain.dietitians.services

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian
import be.ehb.gdt.nutrisearch.domain.dietitians.valueobjects.DietitianRegistrationRecord

interface DietitianService {
    fun getDietitians(): List<Dietitian>
    fun registerDietitian(registrationRecord: DietitianRegistrationRecord)
    fun updateDietitian(registrationRecord: DietitianRegistrationRecord)
    fun deleteDietitian(id: String)
}


