package be.ehb.gdt.nutrisearch.domain.dish.valueobjects

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType

class Product(
    val name: String,
    val brand: String?,
    val preparation: Preparation,
    val amount: Int,
    @Field(targetType = FieldType.OBJECT_ID)
    val id: String
)