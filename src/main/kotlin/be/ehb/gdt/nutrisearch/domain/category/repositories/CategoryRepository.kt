package be.ehb.gdt.nutrisearch.domain.category.repositories

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory

interface CategoryRepository {
    fun findAllCategories(): List<Category>
    fun findCategory(id: String): Category?
    fun findSubcategoriesByCategoryId(id: String): List<Subcategory>?
    fun findSubcategoryById(parentId: String, id: String): Subcategory?
    fun insertCategory(category: Category): Category
    fun insertSubcategory(parentId: String, subcategory: Subcategory): Subcategory
    fun updateCategory(id: String, name: String)
    fun updateSubcategory(parentId: String, subcategory: Subcategory)
    fun deleteCategoryById(id: String)
    fun deleteSubcategoryById(parentId: String, id: String)
    fun existsCategoryById(id: String): Boolean
    fun existsSubcategoryById(parentId: String, id: String): Boolean
    fun countSubcategories(id: String): Int
}