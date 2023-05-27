package be.ehb.gdt.nutrisearch.domain.consumption.entities

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

class Consumption(
    val meal: Meal,
    val product: Product,
    val servingSize: ServingSize,
    val preparation: Preparation,
    val amount: Int,
    val timestamp: LocalDateTime,
    val ownerId: String,
    @Id val id: String = ObjectId.get().toHexString()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Consumption

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}