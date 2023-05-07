package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.services.SubcategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories/{parentId}/subcategories")
class SubcategoriesController(private val service: SubcategoryService) {
    @GetMapping
    fun getSubcategories(@PathVariable parentId: String) = ResponseEntity.ok(service.getSubcategories(parentId))

    @GetMapping("/{id}")
    fun getSubcategory(@PathVariable parentId: String, @PathVariable id: String) =
        ResponseEntity.ok(service.getSubcategory(parentId, id))

    @PostMapping
    fun postSubcategory(
        @PathVariable parentId: String,
        @RequestParam name: String,
    ): ResponseEntity<Unit> {
        service.createSubcategory(parentId, name)
        return ResponseEntity.noContent().header("Content-Type").build()
    }

    @PutMapping("/{id}")
    fun putSubcategory(
        @PathVariable parentId: String,
        @PathVariable id: String,
        @RequestParam name: String
    ): ResponseEntity<Unit> {
        service.updateSubcategory(parentId, id, name)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteSubcategory(@PathVariable parentId: String, @PathVariable id: String): ResponseEntity<Unit> {
        service.deleteSubcategory(parentId, id)
        return ResponseEntity.noContent().build()
    }
}