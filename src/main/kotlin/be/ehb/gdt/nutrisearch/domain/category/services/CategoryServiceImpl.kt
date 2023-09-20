package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.repositories.CategoryRepository
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(private val repo: CategoryRepository) : CategoryService {
    override fun getCategories() = repo.findAllCategories()

    override fun getCategory(id: String) =
        repo.findCategory(id) ?: throw ResourceNotFoundException(Category::class.java, id)

    override fun createCategory(category: Category): Category {
        check(category.subcategories.isNotEmpty()) { "Category needs to contain at least one subcategory" }
        return repo.insertCategory(category)
    }

    override fun updateCategory(id: String, category: Category) {
        if (category.id != id) {
            throw ResourceDoesNotMatchIdException(category.id, id)
        }
        if (!repo.existsCategoryById(id)) {
            throw ResourceNotFoundException(Category::class.java, id)
        }
        check(category.subcategories.isNotEmpty()) {
            "Category needs to contain at least one subcategory"
        }
        repo.updateCategory(id, category.name)
    }

    override fun deleteCategory(id: String) {
        if (!repo.existsCategoryById(id)) {
            throw ResourceNotFoundException(Category::class.java, id)
        }
        repo.deleteCategoryById(id)
    }
}