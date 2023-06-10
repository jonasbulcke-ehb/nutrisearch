package be.ehb.gdt.nutrisearch.restapi.category

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.services.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories")
class CategoriesRestController(private val service: CategoryService) {

    @GetMapping
    fun getCategories() = service.getCategories()

    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: String) = service.getCategory(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postCategory(@RequestBody category: Category) = service.createCategory(category)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putCategory(@PathVariable id: String, @RequestBody category: Category) = service.updateCategory(id, category)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMapping(@PathVariable id: String) = service.deleteCategory(id)
}

