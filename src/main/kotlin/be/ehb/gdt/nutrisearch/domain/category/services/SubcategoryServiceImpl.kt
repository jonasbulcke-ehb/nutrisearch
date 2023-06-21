package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.category.repositories.SubcategoryRepository
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class SubcategoryServiceImpl(private val repo: SubcategoryRepository) : SubcategoryService {
    override fun getSubcategories(parentId: String): List<Subcategory> {
        if (!repo.existsParentCategoryById(parentId)) {
            throw ResourceNotFoundException("Category", parentId)
        }
        return repo.findAllSubcategories(parentId)
    }

    override fun getSubcategory(parentId: String, id: String): Subcategory {
        if (!repo.existsParentCategoryById(parentId)) {
            throw ResourceNotFoundException("Category", parentId)
        }
        return repo.findSubcategory(parentId, id) ?: throw ResourceNotFoundException(Subcategory::class.java, id)
    }

    override fun createSubcategory(parentId: String, name: String): Subcategory {
        if (!repo.existsParentCategoryById(parentId)) {
            throw ResourceNotFoundException("Category", parentId)
        }
        val subcategory = Subcategory(name)
        repo.insertSubcategory(parentId, subcategory)
        return subcategory
    }

    override fun updateSubcategory(parentId: String, id: String, name: String) {
        if (!repo.existsSubcategoryById(parentId, id)) {
            throw ResourceNotFoundException(Subcategory::class.java, id)
        }
        repo.updateSubcategory(parentId, Subcategory(name, id))
    }

    override fun deleteSubcategory(parentId: String, id: String) {
        if (!repo.existsSubcategoryById(parentId, id)) {
            throw ResourceNotFoundException(Subcategory::class.java, id)
        }
        check(repo.countSubcategoriesByCategoryId(parentId) > 1) {
            "Category needs to contain at least one subcategory"
        }
        repo.deleteSubcategory(parentId, id)
    }
}