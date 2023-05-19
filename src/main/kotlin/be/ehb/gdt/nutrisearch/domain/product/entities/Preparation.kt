package be.ehb.gdt.nutrisearch.domain.product.entities

import org.bson.types.ObjectId


class Preparation(
    val name: String,
    val nutrients: Map<Nutrient, Int> = emptyMap(),
    val id: String = ObjectId.get().toHexString()
)