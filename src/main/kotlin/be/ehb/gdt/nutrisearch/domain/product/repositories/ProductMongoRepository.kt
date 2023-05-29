package be.ehb.gdt.nutrisearch.domain.product.repositories

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@SuppressWarnings("kotlin:S6518")
@Repository
class ProductMongoRepository(private val mongoTemplate: MongoTemplate) : ProductRepository {
    override fun findAllProducts(): List<Product> {
        val query = Query()
        query.fields().exclude("servingSizes", "preparations")
        return mongoTemplate.find(query, Product::class.java)
    }

    override fun findProductById(id: String): Product? = mongoTemplate.findById(id, Product::class.java)

    override fun saveProduct(product: Product): Product = mongoTemplate.save(product)

    override fun verifyProduct(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("isVerified", true)
        mongoTemplate.updateFirst(query, update, Product::class.java)
    }

    override fun deleteProductById(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query, Category::class.java)
    }

    override fun existProductById(id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.exists(query, Category::class.java)
    }

    override fun belongProductToOwnerId(id: String, ownerId: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id).and("ownerId").`is`(ownerId))
        return mongoTemplate.exists(query, Category::class.java)
    }
}