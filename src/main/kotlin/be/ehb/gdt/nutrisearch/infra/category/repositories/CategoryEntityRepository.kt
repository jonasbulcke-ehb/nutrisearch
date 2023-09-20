package be.ehb.gdt.nutrisearch.infra.category.repositories

import be.ehb.gdt.nutrisearch.infra.category.entities.CategoryEntity
import be.ehb.gdt.nutrisearch.infra.category.entities.SubcategoryEntity

interface CategoryEntityRepository {
    fun findAllCategories(): List<CategoryEntity>
    fun findCategoryById(id: String): CategoryEntity?
    fun insertCategory(category: CategoryEntity): CategoryEntity
    fun insertSubcategory(parentId: String, subcategory: SubcategoryEntity): SubcategoryEntity
    fun updateCategory(id: String, name: String)
    fun updateSubcategory(parentId: String, subcategory: SubcategoryEntity)
    fun deleteCategoryById(id: String)
    fun deleteSubcategoryId(parentId: String, id: String)
    fun existsCategory(id: String): Boolean
    fun existsSubcategory(parentId: String, id: String): Boolean
    fun countSubcategories(id: String): Int
}