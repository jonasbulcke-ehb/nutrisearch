package be.ehb.gdt.nutrisearch.restapi.study

import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.services.StudyService
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/studies")
class StudiesRestController(private val studyService: StudyService) {

    @GetMapping
    fun getStudies() = studyService.getStudies()

    @GetMapping("/{id}")
    fun getStudy(@PathVariable id: String) = studyService.getStudy(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postStudy(@RequestBody study: Study) = studyService.createStudy(study)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putStudy(@PathVariable id: String, @RequestBody study: UpdatableStudy) = studyService.updateStudy(id, study)

    @PutMapping("/{id}/terminate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun terminateStudy(@PathVariable id: String) = studyService.terminateStudy(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudy(@PathVariable id: String) = studyService.deleteStudy(id)

    @PostMapping("/{id}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun joinStudy(@PathVariable id: String) = studyService.joinStudy(id)

    @DeleteMapping("/{id}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun leaveStudy(@PathVariable id: String) = studyService.leaveStudy(id)

}