package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.services.SubcategoryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories/{parentId}/subcategories")
class SubcategoriesRestController(private val service: SubcategoryService) {
    @GetMapping
    fun getSubcategories(@PathVariable parentId: String) = service.getSubcategories(parentId)

    @GetMapping("/{id}")
    fun getSubcategory(@PathVariable parentId: String, @PathVariable id: String) =
        service.getSubcategory(parentId, id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postSubcategory(@PathVariable parentId: String, @RequestParam name: String) =
        service.createSubcategory(parentId, name)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putSubcategory(@PathVariable parentId: String, @PathVariable id: String, @RequestParam name: String) =
        service.updateSubcategory(parentId, id, name)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSubcategory(@PathVariable parentId: String, @PathVariable id: String) =
        service.deleteSubcategory(parentId, id)
}