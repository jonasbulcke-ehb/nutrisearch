package be.ehb.gdt.nutrisearch.domain.category.services

import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory

interface SubcategoryService {
    fun getSubcategories(parentId: String): List<Subcategory>
    fun getSubcategory(parentId: String, id: String): Subcategory
    fun createSubcategory(parentId: String, name: String): Subcategory
    fun updateSubcategory(parentId: String, id: String, name: String)
    fun deleteSubcategory(parentId: String, id: String)
}