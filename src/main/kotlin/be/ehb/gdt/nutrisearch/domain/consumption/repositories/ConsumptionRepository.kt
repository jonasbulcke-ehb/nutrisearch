package be.ehb.gdt.nutrisearch.domain.consumption.repositories

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import java.time.LocalDateTime

interface ConsumptionRepository {
    fun findConsumptionsByTimestampAndOwnerId(timestamp: LocalDateTime, ownerId: String)
    fun findConsumptionById(id: String)
    fun saveConsumption(consumption: Consumption)
    fun deleteConsumption(id: String)
}