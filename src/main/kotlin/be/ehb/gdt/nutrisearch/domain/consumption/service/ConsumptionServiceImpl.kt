package be.ehb.gdt.nutrisearch.domain.consumption.service

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.repositories.ConsumptionRepository
import be.ehb.gdt.nutrisearch.domain.exceptions.ForbiddenOperationException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ConsumptionServiceImpl(
    private val consumptionRepo: ConsumptionRepository,
    private val userInfoRepo: UserInfoRepository,
    private val authFacade: AuthenticationFacade
) : ConsumptionService {

    override fun getConsumptionsByTimestamp(timestamp: LocalDate): List<Consumption> {
        val userInfoId = getUserInfoId()
        return consumptionRepo.findConsumptionsByTimestampAndUserInfoId(timestamp, userInfoId)
    }

    override fun getConsumptionById(id: String): Consumption {
        val consumption = consumptionRepo.findConsumptionById(id) ?: throw ResourceNotFoundException(Consumption::class.java, id)
        if (consumption.userInfoId != getUserInfoId() && !authFacade.isInRole("dietitian")) {
            throw ForbiddenOperationException("Forbidden to retrieve consumption with id $id")
        }
        return consumption
    }

    override fun createConsumption(consumption: Consumption): Consumption {
        return consumption.apply {
            userInfoId = getUserInfoId()
        }.also {
            consumptionRepo.saveConsumption(it)
        }
    }

    override fun updateConsumption(id: String, consumption: Consumption) {
        if (consumption.id != id) {
            throw ResourceDoesNotMatchIdException(consumption.id, id)
        }

        if (!consumptionRepo.existsConsumptionById(id)) {
            throw ResourceNotFoundException(Consumption::class.java, id)
        }

        val userInfoId = getUserInfoId()

        if (!consumptionRepo.belongsConsumptionToUser(id, userInfoId)) {
            throw ForbiddenOperationException("Forbidden to modify consumption with id $id")
        }

        consumption.apply {
            this.userInfoId = userInfoId
        }.also {
            consumptionRepo.saveConsumption(it)
        }
    }

    override fun deleteConsumption(id: String) {
        val userInfoId = getUserInfoId()

        if (!consumptionRepo.belongsConsumptionToUser(id, userInfoId)) {
            throw ForbiddenOperationException("Forbidden to delete consumption with id $id")
        }

        consumptionRepo.deleteConsumption(id)
    }

    private fun getUserInfoId() =
        userInfoRepo.findUserInfoIdByAuthId(authFacade.authentication.name) ?: throw NoUserInfoForAuthenticationFound()
}