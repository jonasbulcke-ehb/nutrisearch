package be.ehb.gdt.nutrisearch.domain.consumption.service

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.exceptions.ConsumptionNotFoundException
import be.ehb.gdt.nutrisearch.domain.consumption.repositories.ConsumptionRepository
import be.ehb.gdt.nutrisearch.domain.exceptions.ForbiddenOperationException
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ConsumptionServiceImpl(
    private val consumptionRepo: ConsumptionRepository,
    private val userInfoRepo: UserInfoRepository
) : ConsumptionService {

    override fun getConsumptionsByTimestamp(authId: String, consumedAt: Date): List<Consumption> {
        val userInfoId = getUserInfoId(authId)
        return consumptionRepo.findConsumptionsByTimestampAndUserInfoId(consumedAt, userInfoId)
    }

    override fun getConsumptionById(id: String, authId: String): Consumption {
        val userInfoId = getUserInfoId(authId)
        val consumption = consumptionRepo.findConsumptionById(id) ?: throw ConsumptionNotFoundException(id)
        if (consumption.userInfoId != userInfoId) {
            throw RuntimeException()
        }
        return consumption
    }

    override fun createConsumption(authId: String, consumption: Consumption): Consumption {
        return consumption.apply {
            userInfoId = getUserInfoId(authId)
        }.also {
            consumptionRepo.saveConsumption(it)
        }
    }

    override fun updateConsumption(id: String, authId: String, consumption: Consumption) {
        if (!consumptionRepo.existsConsumptionById(id)) {
            throw ConsumptionNotFoundException(id)
        }

        val userInfoId = getUserInfoId(authId)

        if (consumption.userInfoId != userInfoId || !consumptionRepo.belongsConsumptionToUser(id, userInfoId)) {
            throw ForbiddenOperationException("Forbidden to modify consumption with id $id")
        }

        consumptionRepo.saveConsumption(consumption)
    }

    override fun deleteConsumption(id: String, authId: String) {
        if (!consumptionRepo.existsConsumptionById(id)) {
            throw ConsumptionNotFoundException(id)
        }

        val userInfoId = getUserInfoId(authId)

        if (!consumptionRepo.belongsConsumptionToUser(id, userInfoId)) {
            throw ForbiddenOperationException("Forbidden to delete consumption with id $id")
        }

        consumptionRepo.deleteConsumption(id)
    }

    private fun getUserInfoId(authId: String) =
        userInfoRepo.findUserInfoIdByAuthId(authId) ?: throw NoUserInfoForAuthenticationFound()
}