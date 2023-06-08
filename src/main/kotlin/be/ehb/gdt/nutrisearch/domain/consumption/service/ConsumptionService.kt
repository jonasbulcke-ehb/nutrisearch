package be.ehb.gdt.nutrisearch.domain.consumption.service

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import java.time.LocalDate

interface ConsumptionService {
    fun getConsumptionsByTimestamp(timestamp: LocalDate): List<Consumption>
    fun getConsumptionById(id: String): Consumption
    fun createConsumption(consumption: Consumption): Consumption
    fun updateConsumption(id: String, consumption: Consumption)
    fun deleteConsumption(id: String)
}
