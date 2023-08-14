package be.ehb.gdt.nutrisearch.restapi.consumption

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.consumption.services.ConsumptionService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@RequestMapping("/api/v1/consumptions")
class ConsumptionsRestController(private val service: ConsumptionService) {
    @GetMapping
    fun getConsumptions(@RequestParam timestamp: LocalDate) = service.getConsumptionsByTimestamp(timestamp)

    @GetMapping("/{id}")
    fun getConsumption(@PathVariable id: String) = service.getConsumptionById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postConsumption(@RequestBody consumption: Consumption) =
        service.createConsumption(consumption)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putConsumption(@PathVariable id: String, @RequestBody consumption: Consumption) =
        service.updateConsumption(id, consumption)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteConsumption(@PathVariable id: String) = service.deleteConsumption(id)

    @GetMapping("/export-to-excel")
    fun exportConsumptions(@RequestParam timestamp: LocalDate, @RequestParam("user-info-id") userInfoId: String, response: HttpServletResponse) {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"))
        response.apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
            setHeader("Content-Disposition", "attachment; filename= $currentTime.xlsx")
        }.also {
            service.exportToExcel(timestamp, userInfoId, response.outputStream)
        }
    }
}