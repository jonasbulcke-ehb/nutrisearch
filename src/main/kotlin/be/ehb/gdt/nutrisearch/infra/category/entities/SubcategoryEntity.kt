package be.ehb.gdt.nutrisearch.infra.category.entities

import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory

class SubcategoryEntity(
    val name: String,
    val id: String,
) {

    fun toDomainObject() = Subcategory(name, id)

    companion object {
        fun fromDomainObject(subcategory: Subcategory): SubcategoryEntity {
            return SubcategoryEntity(
                subcategory.name,
                subcategory.id
            )
        }
    }
}