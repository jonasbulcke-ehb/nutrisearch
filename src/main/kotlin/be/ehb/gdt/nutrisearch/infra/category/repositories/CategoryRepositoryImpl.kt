package be.ehb.gdt.nutrisearch.infra.category.repositories

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import be.ehb.gdt.nutrisearch.domain.category.repositories.CategoryRepository
import be.ehb.gdt.nutrisearch.infra.category.entities.CategoryEntity
import be.ehb.gdt.nutrisearch.infra.category.entities.SubcategoryEntity
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryImpl(private val repository: CategoryEntityRepository) : CategoryRepository {
    override fun findAllCategories() = repository.findAllCategories().map(CategoryEntity::toDomainObject)

    override fun findCategory(id: String) = repository.findCategoryById(id)?.toDomainObject()
    override fun findSubcategoriesByCategoryId(id: String) =
        repository.findCategoryById(id)?.subcategories?.map(SubcategoryEntity::toDomainObject)

    override fun findSubcategoryById(parentId: String, id: String) =
        repository.findCategoryById(parentId)
            ?.subcategories
            ?.first { it.id == id }
            ?.toDomainObject()

    override fun insertCategory(category: Category) =
        repository.insertCategory(CategoryEntity.fromDomainObject(category)).toDomainObject()

    override fun insertSubcategory(parentId: String, subcategory: Subcategory) =
        repository.insertSubcategory(parentId, SubcategoryEntity.fromDomainObject(subcategory)).toDomainObject()

    override fun updateCategory(id: String, name: String) = repository.updateCategory(id, name)
    override fun updateSubcategory(parentId: String, subcategory: Subcategory) =
        repository.updateSubcategory(parentId, SubcategoryEntity.fromDomainObject(subcategory))

    override fun deleteCategoryById(id: String) = repository.deleteCategoryById(id)
    override fun deleteSubcategoryById(parentId: String, id: String) = repository.deleteSubcategoryId(parentId, id)

    override fun existsCategoryById(id: String) = repository.existsCategory(id)
    override fun existsSubcategoryById(parentId: String, id: String) =
        repository.existsSubcategory(parentId, id)

    override fun countSubcategories(id: String) = repository.countSubcategories(id)
}