package be.ehb.gdt.nutrisearch.domain.category.repositories

import be.ehb.gdt.nutrisearch.domain.category.entities.Category

interface CategoryRepository {
    fun findAllCategories(): List<Category>
    fun findCategory(id: String): Category?
    fun insertCategory(category: Category): Category
    fun updateCategory(id: String, name: String)
    fun deleteCategory(id: String)
    fun existsCategoryById(id: String): Boolean
}