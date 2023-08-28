package be.ehb.gdt.nutrisearch.domain.consumption.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.DishConsumption
import java.io.OutputStream
import java.time.LocalDate

interface ConsumptionService {
    fun getConsumptionsByTimestamp(timestamp: LocalDate): List<Consumption>
    fun getConsumptionById(id: String): Consumption
    fun createConsumption(consumption: Consumption): Consumption
    fun updateConsumption(id: String, consumption: Consumption)
    fun deleteConsumption(id: String)
    fun exportToExcel(timestamp: LocalDate, id: String, outputStream: OutputStream)
    fun createConsumptionsFromDish(model: DishConsumption): List<Consumption>
}
