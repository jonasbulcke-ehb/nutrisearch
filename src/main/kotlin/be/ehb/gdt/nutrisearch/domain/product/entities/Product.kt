package be.ehb.gdt.nutrisearch.domain.product.entities

import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("products")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Product(
    brand: String?,
    @Indexed
    val name: String,
    @Indexed
    val categoryId: String,
    var isVerified: Boolean = false,
    val preparations: MutableList<Preparation> = mutableListOf(),
    val servingSizes: MutableSet<ServingSize> = mutableSetOf(),
    @Id
    val id: String = ObjectId.get().toHexString(),
) {
    val brand: String?

    @Indexed
    var ownerId: String? = null

    init {
        this.brand = if (brand?.isBlank() == true) null else brand
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

