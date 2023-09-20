package be.ehb.gdt.nutrisearch.domain.dish.repositories

import be.ehb.gdt.nutrisearch.domain.dish.entities.Dish
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import be.ehb.gdt.nutrisearch.domain.product.entities.Product as CompleteProduct

@Repository
class DishMongoRepository(private val mongoTemplate: MongoTemplate) : DishRepository {
    override fun findAllDishesByOwnerId(ownerId: String): List<Dish> =
        Query(Criteria.where("ownerId").`is`(ownerId)).let { mongoTemplate.find(it, Dish::class.java) }

    override fun findDishById(id: String) = mongoTemplate.findById(id, Dish::class.java)

    override fun findDishByIdAndOwnerId(id: String, ownerId: String) =
        Query(Criteria.where("_id").`is`(id).and("ownerId").`is`(ownerId)).let {
            mongoTemplate.findOne(it, Dish::class.java)
        }

    override fun saveDish(dish: Dish) = mongoTemplate.save(dish)

    override fun findCompleteProducts(id: String): List<CompleteProduct> {
        val matchStage = Aggregation.match(Criteria.where("_id").`is`(id))
        val lookupStage = Aggregation.lookup("products", "products._id", "_id", "products")
        val unwindStage = Aggregation.unwind("products")
        val replaceRootStage = Aggregation.replaceRoot("products")
        return mongoTemplate.aggregate(
            Aggregation.newAggregation(matchStage, lookupStage, unwindStage, replaceRootStage),
            Dish::class.java,
            CompleteProduct::class.java
        ).mappedResults
    }

    override fun deleteDish(id: String) {
        Query(Criteria.where("_id").`is`(id)).let { mongoTemplate.remove(it, Dish::class.java) }
    }

    override fun deleteDishesByUserinfoId(userinfoId: String) {
        Query(Criteria.where("ownerId").`is`(userinfoId)).let { mongoTemplate.remove(it, Dish::class.java) }
    }

    override fun existsById(id: String) =
        Query(Criteria.where("_id").`is`(id)).let { mongoTemplate.exists(it, Dish::class.java) }

    override fun belongsToUserInfoId(id: String, ownerId: String) =
        Query(Criteria.where("_id").`is`(id).and("ownerId").`is`(ownerId)).let {
            mongoTemplate.exists(it, Dish::class.java)
        }

}