package be.ehb.gdt.nutrisearch.restapi.patients

import be.ehb.gdt.nutrisearch.domain.patients.services.PatientService
import be.ehb.gdt.nutrisearch.domain.questionnaire.services.QuestionnaireService
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.NameRecord
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("api/v1/patients")
class PatientsRestController(
    private val service: PatientService,
    private val questionnaireService: QuestionnaireService
) {
    @GetMapping
    fun getPatients(): List<NameRecord> = service.getPatients()

    @GetMapping("/{id}")
    fun getUserInfo(@PathVariable id: String) = service.getPatientInfo(id)

    @GetMapping("/{id}/consumptions")
    fun getConsumptions(@PathVariable id: String, @RequestParam timestamp: LocalDate) =
        service.getConsumptions(id, timestamp)

    @GetMapping("/{id}/export-to-excel")
    fun exportConsumptions(
        @PathVariable id: String,
        @RequestParam timestamp: LocalDate,
        response: HttpServletResponse
    ) {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"))
        response.apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
            setHeader("Content-Disposition", "attachment; filename= $currentTime.xlsx")
        }.also {
            service.exportConsumptionsToExcel(id, timestamp, response.outputStream)
        }
    }

    @GetMapping("/{id}/questionnaire")
    fun getQuestionnaire(@PathVariable id: String, @RequestParam date: LocalDate) =
        questionnaireService.getQuestionnaire(id, date)
}
