package be.ehb.gdt.nutrisearch.domain.consumption.repositories

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import java.time.LocalDateTime
import java.util.Date

interface ConsumptionRepository {
    fun findConsumptionsByTimestampAndUserInfoId(consumedAt: Date, userInfoId: String): List<Consumption>
    fun findConsumptionById(id: String): Consumption?
    fun saveConsumption(consumption: Consumption): Consumption
    fun deleteConsumption(id: String)
    fun existsConsumptionById(id: String): Boolean
    fun belongsConsumptionToUser(id: String, userInfoId: String): Boolean
}