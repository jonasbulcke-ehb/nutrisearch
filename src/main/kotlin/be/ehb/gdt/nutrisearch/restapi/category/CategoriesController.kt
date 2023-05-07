package be.ehb.gdt.nutrisearch_api.controllers

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.services.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories")
class CategoriesController(private val service: CategoryService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    fun getCategories() = ResponseEntity.ok(service.getCategories())

    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: String): ResponseEntity<Category> = ResponseEntity.ok(service.getCategory(id))

    @PostMapping
    fun postCategory(@RequestParam name: String): ResponseEntity<Category> =
        ResponseEntity(service.createCategory(name), HttpStatus.CREATED)

    @PutMapping("/{id}")
    fun putCategory(@PathVariable id: String, @RequestParam name: String): ResponseEntity<Unit> {
        service.updateCategory(id, name)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteMapping(@PathVariable id: String): ResponseEntity<Unit> {
        service.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }
}

