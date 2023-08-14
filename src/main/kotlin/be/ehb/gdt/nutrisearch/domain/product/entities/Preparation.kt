package be.ehb.gdt.nutrisearch.domain.product.entities

import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import org.bson.types.ObjectId


class Preparation(
    val name: String,
    val nutrients: Map<Nutrient, Double?> = emptyMap(),
    val id: String = ObjectId.get().toHexString()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Preparation

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}