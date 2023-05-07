package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.exceptions.CategoryNotFoundException
import be.ehb.gdt.nutrisearch.domain.category.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(private val repo: CategoryRepository) : CategoryService {
    override fun getCategories() = repo.findAllCategories()

    override fun getCategory(id: String) =
        repo.findCategory(id) ?: throw CategoryNotFoundException(id)

    override fun createCategory(name: String) = repo.insertCategory(Category(name))

    override fun updateCategory(id: String, name: String) {
        if(!repo.existsCategoryById(id)) {
            throw CategoryNotFoundException(id)
        }
        repo.updateCategory(id, name)
    }

    override fun deleteCategory(id: String) {
        if(!repo.existsCategoryById(id)) {
            throw CategoryNotFoundException(id)
        }
        repo.deleteCategory(id)
    }
}