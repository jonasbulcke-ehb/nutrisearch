package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.services.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories")
class CategoriesController(private val service: CategoryService) {

    @GetMapping
    fun getCategories() = service.getCategories()

    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: String) = service.getCategory(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCategory(@RequestParam name: String) =
        service.createCategory(name)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putCategory(@PathVariable id: String, @RequestParam name: String) = service.updateCategory(id, name)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMapping(@PathVariable id: String) = service.deleteCategory(id)
}

