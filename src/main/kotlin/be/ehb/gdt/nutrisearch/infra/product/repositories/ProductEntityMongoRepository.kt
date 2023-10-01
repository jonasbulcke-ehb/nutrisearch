package be.ehb.gdt.nutrisearch.infra.product.repositories

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.infra.product.entities.ProductEntity
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@SuppressWarnings("kotlin:S6518")
@Repository
class ProductEntityMongoRepository(private val mongoTemplate: MongoTemplate) : ProductEntityRepository {
    override fun findAllProducts(): List<ProductEntity> {
        val query = Query()
        query.fields().exclude("servingSizes", "preparations")
        return mongoTemplate.find(query, ProductEntity::class.java)
    }

    override fun findAllProductsByOwnerId(ownerId: String): List<ProductEntity> {
        val query = Query(Criteria.where("ownerId").`is`(ownerId))
        return mongoTemplate.find(query, ProductEntity::class.java)
    }

    override fun findProductById(id: String): ProductEntity? = mongoTemplate.findById(id, ProductEntity::class.java)

    override fun saveProduct(product: ProductEntity): ProductEntity = mongoTemplate.save(product)

    override fun insertProducts(products: List<ProductEntity>) {
        mongoTemplate.insert(products, ProductEntity::class.java)
    }

    override fun verifyProduct(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("isVerified", true)
        mongoTemplate.updateFirst(query, update, ProductEntity::class.java)
    }

    override fun deleteProductById(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query, ProductEntity::class.java)
    }

    override fun existsProductById(id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.exists(query, ProductEntity::class.java)
    }

    override fun belongsProductToOwnerId(id: String, ownerId: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id).and("ownerId").`is`(ownerId))
        return mongoTemplate.exists(query, ProductEntity::class.java)
    }

    override fun findFavoriteProductsByAuthId(authId: String) : List<ProductEntity> {
        val matchStage = Aggregation.match(Criteria.where("authId").`is`(authId))
        val lookupStage = Aggregation.lookup("products", "favoriteProductIds", "_id", "favoriteProducts")
        val unwindStage = Aggregation.unwind("favoriteProducts")
        val replaceRootStage = Aggregation.replaceRoot("favoriteProducts")

        return mongoTemplate.aggregate(
            Aggregation.newAggregation(matchStage, lookupStage, unwindStage, replaceRootStage),
            UserInfo::class.java,
            ProductEntity::class.java
        ).mappedResults
    }
}