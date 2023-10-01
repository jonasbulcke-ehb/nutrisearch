package be.ehb.gdt.nutrisearch.infra.product.entities

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("products")
class ProductEntity(
    @Indexed
    val brand: String?,
    @Indexed
    val name: String,
    @Indexed
    val categoryId: String,
    var isVerified: Boolean,
    val preparations: MutableList<Preparation>,
    val servingSizes: MutableSet<ServingSize>,
    @Indexed
    var ownerId: String?,
    @Id
    val id: String
) {

    fun toDomainObject(): Product {
        return Product(
            brand, name, categoryId, isVerified, preparations, servingSizes, id
        ).apply {
            ownerId = this@ProductEntity.ownerId
        }
    }

    companion object {
        fun fromDomainObject(product: Product): ProductEntity {
            return product.run {
                ProductEntity(
                    brand, name, categoryId, isVerified, preparations, servingSizes, ownerId, id
                )
            }
        }
    }
}