package be.ehb.gdt.nutrisearch.restapi.dietitians

import be.ehb.gdt.nutrisearch.domain.dietitians.services.DietitianService
import be.ehb.gdt.nutrisearch.domain.dietitians.valueobjects.DietitianRegistrationRecord
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/dietitians")
class DietitiansRestController(private val service: DietitianService) {

    @GetMapping
    fun getDietitians() = service.getDietitians()

    @PostMapping
    fun postDietitian(@RequestBody registrationRecord: DietitianRegistrationRecord) =
        service.registerDietitian(registrationRecord)
}