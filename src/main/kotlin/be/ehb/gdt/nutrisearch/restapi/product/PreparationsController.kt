package be.ehb.gdt.nutrisearch.restapi.product

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.services.PreparationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products/{productId}/preparations")
class PreparationsController(private val service: PreparationService) {
    @GetMapping
    fun getPreparations(@PathVariable productId: String) = service.getPreparations(productId)

    @GetMapping("/{id}")
    fun getPreparation(@PathVariable productId: String, @PathVariable id: String) =
        service.getPreparation(productId, id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postPreparation(@PathVariable productId: String, @RequestBody preparation: Preparation) =
        service.createPreparation(productId, preparation)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putPreparation(
        @PathVariable productId: String,
        @PathVariable id: String,
        @RequestBody preparation: Preparation
    ) = service.updatePreparation(productId, id, preparation)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePreparation(@PathVariable productId: String, @PathVariable id: String) =
        service.deletePreparation(productId, id)
}