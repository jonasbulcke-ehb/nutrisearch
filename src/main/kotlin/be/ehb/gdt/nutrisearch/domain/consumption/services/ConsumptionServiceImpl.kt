package be.ehb.gdt.nutrisearch.domain.consumption.services

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.repositories.ConsumptionRepository
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.DishConsumption
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Product
import be.ehb.gdt.nutrisearch.domain.exceptions.ForbiddenOperationException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.excel.UserConsumptionsExcelWriter
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.stereotype.Component
import java.io.OutputStream
import java.time.LocalDate

@Component
class ConsumptionServiceImpl(
    private val consumptionRepo: ConsumptionRepository,
    private val userInfoRepo: UserInfoRepository,
    private val authFacade: AuthenticationFacade
) : ConsumptionService {
    private val userInfoId: String
        get() = userInfoRepo.findUserInfoIdByAuthId(authFacade.authentication.name)
            ?: throw NoUserInfoForAuthenticationFound()

    override fun getConsumptionsByTimestamp(timestamp: LocalDate): List<Consumption> {
        return consumptionRepo.findConsumptionsByTimestampAndUserInfoId(timestamp, userInfoId)
    }

    override fun getConsumptionById(id: String): Consumption {
        val consumption =
            consumptionRepo.findConsumptionById(id) ?: throw ResourceNotFoundException(Consumption::class.java, id)
        if (consumption.userInfoId != userInfoId && !authFacade.isInRole("dietitian")) {
            throw ForbiddenOperationException("Forbidden to retrieve consumption with id $id")
        }
        return consumption
    }

    override fun createConsumption(consumption: Consumption): Consumption {
        return consumption.apply {
            userInfoId = this@ConsumptionServiceImpl.userInfoId
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

        if (!consumptionRepo.belongsConsumptionToUser(id, userInfoId)) {
            throw ForbiddenOperationException("Forbidden to modify consumption with id $id")
        }

        consumption.apply {
            userInfoId = this@ConsumptionServiceImpl.userInfoId
        }.also {
            consumptionRepo.saveConsumption(it)
        }
    }

    override fun deleteConsumption(id: String) {
        if (!consumptionRepo.belongsConsumptionToUser(id, userInfoId)) {
            throw ForbiddenOperationException("Forbidden to delete consumption with id $id")
        }

        consumptionRepo.deleteConsumption(id)
    }

    override fun exportToExcel(timestamp: LocalDate, id: String, outputStream: OutputStream) {
        consumptionRepo.findConsumptionsByTimestampAndUserInfoId(timestamp, id).also {
            UserConsumptionsExcelWriter(it, timestamp.toString()).write(outputStream)
        }
    }

    override fun createConsumptionsFromDish(model: DishConsumption): List<Consumption> {
        return model.dish.products.map {
            Consumption(
                model.meal,
                Product(it.brand, it.name, it.id),
                ServingSize(),
                it.preparation,
                it.amount * model.amount,
                model.timestamp,
            ).apply {
                userInfoId = this@ConsumptionServiceImpl.userInfoId
            }
        }.also {
            consumptionRepo.insertConsumptions(it)
        }
    }

}