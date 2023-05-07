package be.ehb.gdt.nutrisearch.domain.category.repositories

import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory

interface SubcategoryRepository {
    fun findAllSubcategories(parentId: String): List<Subcategory>
    fun findSubcategory(parentId: String, id: String): Subcategory?
    fun insertSubcategory(parentId: String, subcategory: Subcategory): Subcategory
    fun updateSubcategory(parentId: String, subcategory: Subcategory)
    fun deleteSubcategory(parentId: String, id: String)
    fun existsParentCategoryById(parentId: String): Boolean
    fun existsSubcategoryById(parentId: String, id: String): Boolean
}