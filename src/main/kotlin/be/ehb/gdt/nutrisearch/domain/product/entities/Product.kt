package be.ehb.gdt.nutrisearch.domain.product.entities

import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("products")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Product(
    @Indexed
    var name: String,
    @Indexed
    var categoryId: String,
    var isVerified: Boolean = false,
    val preparations: MutableList<Preparation> = mutableListOf(),
    val servingSizes: MutableList<ServingSize> = mutableListOf(),
    @Id
    val id: String = ObjectId.get().toHexString(),
)

