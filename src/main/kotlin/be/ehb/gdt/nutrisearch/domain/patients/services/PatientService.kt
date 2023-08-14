package be.ehb.gdt.nutrisearch.domain.patients.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.patients.valueobjects.Patient
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.NameRecord
import java.io.OutputStream
import java.time.LocalDate

interface PatientService {
    fun getPatients(): List<NameRecord>
    fun getPatientInfo(id: String): Patient
    fun getConsumptions(id: String, timestamp: LocalDate): List<Consumption>
    fun exportConsumptionsToExcel(id: String, timestamp: LocalDate, outputStream: OutputStream)
}