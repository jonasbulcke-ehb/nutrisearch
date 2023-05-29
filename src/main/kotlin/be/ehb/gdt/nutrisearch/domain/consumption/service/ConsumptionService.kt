package be.ehb.gdt.nutrisearch.domain.consumption.service

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import java.util.Date

interface ConsumptionService {
    fun getConsumptionsByTimestamp(authId: String, consumedAt: Date): List<Consumption>
    fun getConsumptionById(id: String, authId: String): Consumption
    fun createConsumption(authId: String, consumption: Consumption): Consumption
    fun updateConsumption(id: String, authId: String, consumption: Consumption)
    fun deleteConsumption(id: String, authId: String)
}
