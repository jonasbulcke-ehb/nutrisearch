package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
interface CategoryService {
    fun getCategories(): List<Category>
    fun getCategory(id: String): Category
    fun createCategory(category: Category): Category
    fun updateCategory(id: String, category: Category)
    fun deleteCategory(id: String)
}
