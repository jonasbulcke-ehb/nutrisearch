package be.ehb.gdt.nutrisearch.domain.product.repositories

import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ProductMongoRepository(private val mongoTemplate: MongoTemplate) {
    fun findAllProducts(): List<Product> {
        val query = Query()
        query.fields().exclude("servingSizes", "preparations")
        return mongoTemplate.find(query, Product::class.java)
    }

    fun findProductById(id: String): Product? = mongoTemplate.findById(id, Product::class.java)

    fun saveProduct(product: Product): Product = mongoTemplate.save(product)
}