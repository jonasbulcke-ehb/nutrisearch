package be.ehb.gdt.nutrisearch.domain.consumption.entities

import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Meal
import be.ehb.gdt.nutrisearch.domain.consumption.valueobjects.Product
import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("consumptions")
class Consumption(
    val meal: Meal,
    val product: Product,
    val servingSize: ServingSize,
    val preparation: Preparation,
    val amount: Double,
    val timestamp: LocalDate,
    @Id val id: String = ObjectId.get().toHexString()
) {
    @JsonIgnore
    lateinit var userInfoId: String
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