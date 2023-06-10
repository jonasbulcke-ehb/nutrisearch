package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.exceptions.CategoryNotFoundException
import be.ehb.gdt.nutrisearch.domain.category.exceptions.InvalidCategoryException
import be.ehb.gdt.nutrisearch.domain.category.repositories.CategoryRepository
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(private val repo: CategoryRepository) : CategoryService {
    override fun getCategories() = repo.findAllCategories()

    override fun getCategory(id: String) =
        repo.findCategory(id) ?: throw CategoryNotFoundException(id)

    override fun createCategory(category: Category): Category {
        if (category.subcategories.isEmpty()) {
            throw InvalidCategoryException()
        }
        return repo.saveCategory(category)
    }

    override fun updateCategory(id: String, category: Category) {
        if (category.id != id) {
            throw ResourceDoesNotMatchIdException(category.id, id)
        }
        if (!repo.existsCategoryById(id)) {
            throw CategoryNotFoundException(id)
        }
        if (category.subcategories.isEmpty()) {
            throw InvalidCategoryException()
        }
        repo.saveCategory(category)
    }

    override fun deleteCategory(id: String) {
        if (!repo.existsCategoryById(id)) {
            throw CategoryNotFoundException(id)
        }
        repo.deleteCategory(id)
    }
}