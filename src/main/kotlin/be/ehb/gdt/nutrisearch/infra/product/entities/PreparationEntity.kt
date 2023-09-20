package be.ehb.gdt.nutrisearch.infra.product.entities

import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient

class PreparationEntity(
    val name: String,
    val nutrients: Map<Nutrient, Double?> = emptyMap(),
    val id: String
)