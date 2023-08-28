package be.ehb.gdt.nutrisearch.domain.dish.entities

import be.ehb.gdt.nutrisearch.domain.dish.valueobjects.Product
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType

@Document("dishes")
class Dish(
    val name: String,
    val products: List<Product>,
    @Id
    val id: String = ObjectId().toHexString()
) {
    @Field(targetType = FieldType.OBJECT_ID)
    @JsonIgnore
    lateinit var ownerId: String
}
