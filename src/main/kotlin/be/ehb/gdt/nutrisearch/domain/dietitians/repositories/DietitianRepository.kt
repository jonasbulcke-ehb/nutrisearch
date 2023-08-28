package be.ehb.gdt.nutrisearch.domain.dietitians.repositories

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian

interface DietitianRepository {
    fun findDietitians(): List<Dietitian>
    fun findDietitianById(id: String): Dietitian?
    fun findDietitianByAuthId(authId: String): Dietitian?
    fun insertDietitian(dietitian: Dietitian): Dietitian
    fun updateDietitian(id: String, firstname: String, lastname: String)
    fun deleteDietitian(id: String)
    fun existsById(id: String): Boolean
    fun existsByAuthId(authId: String): Boolean
    fun isTreatingPatient(authId: String, patientId: String): Boolean
}