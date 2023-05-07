package be.ehb.gdt.nutrisearch.domain.category.entities

import org.bson.types.ObjectId

class Subcategory(
    val name: String,
    val id: String = ObjectId.get().toHexString()
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subcategory

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}