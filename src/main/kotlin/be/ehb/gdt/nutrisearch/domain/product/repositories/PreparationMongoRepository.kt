package be.ehb.gdt.nutrisearch.domain.product.repositories

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
@SuppressWarnings("kotlin:S6518")
class PreparationMongoRepository(private val mongoTemplate: MongoTemplate) : PreparationRepository {
    override fun getPreparations(productId: String) =
        mongoTemplate.findById(productId, Product::class.java)?.preparations

    override fun getPreparation(productId: String, id: String): Preparation? {
        return mongoTemplate.findById(productId, Product::class.java)?.preparations?.find { it.id == id }
    }

    override fun insertPreparation(productId: String, preparation: Preparation): Preparation {
        val query = Query(Criteria.where("_id").`is`(productId))
        val update = Update().push("preparations", preparation)
        mongoTemplate.updateFirst(query, update, Product::class.java)
        return preparation
    }

    override fun updatePreparation(productId: String, id: String, preparation: Preparation) {
        val query = Query(Criteria.where("_id").`is`(productId).and("preparations._id").`is`(id))
        val update = Update().set("preparations.$", preparation)
        mongoTemplate.updateFirst(query, update, Product::class.java)
    }

    override fun deletePreparation(productId: String, id: String) {
        val query = Query(Criteria.where("_id").`is`(productId))
        val update = Update().pull("preparations", Query(Criteria.where("_id").`is`(id)))
        mongoTemplate.updateFirst(query, update, Product::class.java)

    }

    override fun existsProductById(productId: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(productId))
        return mongoTemplate.exists(query, Product::class.java)
    }
    override fun existsPreparationsById(productId: String, id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(productId).and("preparations._id").`is`(id))
        return mongoTemplate.exists(query, Product::class.java)
    }

}