package be.ehb.gdt.nutrisearch.infra.category.entities

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("categories")
class CategoryEntity(
    val name: String,
    val subcategories: List<SubcategoryEntity>,
    @Id
    val id: String
) {
    fun toDomainObject(): Category {
        return Category(name, subcategories.map(SubcategoryEntity::toDomainObject).toMutableList(), id)
    }

    companion object {
        fun fromDomainObject(category: Category): CategoryEntity {
            return CategoryEntity(
                category.name,
                category.subcategories.map(SubcategoryEntity.Companion::fromDomainObject),
                category.id
            )
        }
    }
}