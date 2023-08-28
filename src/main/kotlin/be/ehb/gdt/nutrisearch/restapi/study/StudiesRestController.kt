package be.ehb.gdt.nutrisearch.restapi.study

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.services.StudyService
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/v1/studies")
class StudiesRestController(private val service: StudyService) {

    @GetMapping
    fun getStudies() = service.getStudies()

    @GetMapping("/{id}")
    fun getStudy(@PathVariable id: String) = service.getStudy(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postStudy(@RequestBody study: Study) = service.createStudy(study)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putStudy(@PathVariable id: String, @RequestBody study: UpdatableStudy) = service.updateStudy(id, study)

    @PutMapping("/{id}/terminate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun terminateStudy(@PathVariable id: String) = service.terminateStudy(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudy(@PathVariable id: String) = service.deleteStudy(id)

    @PostMapping("/{id}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun joinStudy(@PathVariable id: String) = service.joinStudy(id)

    @DeleteMapping("/{id}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun leaveStudy(@PathVariable id: String) = service.leaveStudy(id)

    @GetMapping("/{id}/export-to-excel")
    fun exportUserConsumptions(@PathVariable id: String, @RequestParam timestamp: LocalDate, response: HttpServletResponse) {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"))
        response.apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE
            setHeader("Content-Disposition", "attachment; filename= $currentTime.xlsx")
        }.also {
            service.exportToExcel(id, timestamp, response.outputStream)
        }
    }

    @GetMapping("/{id}/consumptions")
    fun getConsumptions(@PathVariable id: String, @RequestParam timestamp: LocalDate): List<Consumption> {
        return service.getConsumptions(id, timestamp)
    }

    @GetMapping("/{id}/participants")
    fun getParticipants(@PathVariable id: String) = service.getParticipants(id)
}