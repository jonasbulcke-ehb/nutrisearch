package be.ehb.gdt.nutrisearch.restapi.consumption

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.service.ConsumptionService
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.web.bind.annotation.*
import java.util.Date

@RestController
@RequestMapping("/api/v1/consumptions")
class ConsumptionsRestController(
    private val service: ConsumptionService,
    private val authFacade: AuthenticationFacade
) {

    @GetMapping
    fun getConsumptions(@RequestParam consumedAt: Date) =
        service.getConsumptionsByTimestamp(authFacade.authentication.name, consumedAt)

    @GetMapping("/{id}")
    fun getConsumption(@PathVariable id: String) = service.getConsumptionById(id, authFacade.authentication.name)

    @PostMapping
    fun postConsumption(@RequestBody consumption: Consumption) =
        service.createConsumption(authFacade.authentication.name, consumption)

    @PutMapping("/{id}")
    fun putConsumption(@PathVariable id: String, @RequestBody consumption: Consumption) =
        service.updateConsumption(id, authFacade.authentication.name, consumption)

    @DeleteMapping("/{id}")
    fun deleteConsumption(@PathVariable id: String) = service.deleteConsumption(id, authFacade.authentication.name)
}