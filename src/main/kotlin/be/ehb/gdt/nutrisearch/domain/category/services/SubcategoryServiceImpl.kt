package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.category.exceptions.CategoryNotFoundException
import be.ehb.gdt.nutrisearch.domain.category.exceptions.SubcategoryNotFoundException
import be.ehb.gdt.nutrisearch.domain.category.repositories.SubcategoryRepository
import org.springframework.stereotype.Service

@Service
class SubcategoryServiceImpl(private val repo: SubcategoryRepository): SubcategoryService {
    override fun getSubcategories(parentId: String): List<Subcategory> {
        if (!repo.existsParentCategoryById(parentId)) {
            throw CategoryNotFoundException(parentId)
        }
        return repo.findAllSubcategories(parentId)
    }

    override fun getSubcategory(parentId: String, id: String): Subcategory {
        if(!repo.existsParentCategoryById(parentId)) {
            throw CategoryNotFoundException(parentId)
        }
        return repo.findSubcategory(parentId, id) ?: throw SubcategoryNotFoundException(parentId, id)
    }

    override fun createSubcategory(parentId: String, name: String): Subcategory {
        if (!repo.existsParentCategoryById(parentId)) {
            throw CategoryNotFoundException(parentId)
        }
        val subcategory = Subcategory(name)
        repo.insertSubcategory(parentId, subcategory)
        return subcategory
    }

    override fun updateSubcategory(parentId: String, id: String, name: String) {
        if (!repo.existsSubcategoryById(parentId, id)) {
            throw SubcategoryNotFoundException(parentId, id)
        }
        repo.updateSubcategory(parentId, Subcategory(name, id))
    }

    override fun deleteSubcategory(parentId: String, id: String) {
        if(!repo.existsSubcategoryById(parentId, id)) {
            throw SubcategoryNotFoundException(parentId, id)
        }
        repo.deleteSubcategory(parentId, id)
    }
}