package be.ehb.gdt.nutrisearch.domain.consumption.repositories

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ConsumptionMongoRepository(val mongoTemplate: MongoTemplate) : ConsumptionRepository {

    override fun findConsumptionsByTimestampAndUserInfoId(timestamp: LocalDate, userInfoId: String): List<Consumption> {
        val query = Query(Criteria.where("timestamp").`is`(timestamp.toString()).and("userInfoId").`is`(userInfoId))
        return mongoTemplate.find(query, Consumption::class.java)
    }

    override fun findConsumptionById(id: String) = mongoTemplate.findById(id, Consumption::class.java)

    override fun saveConsumption(consumption: Consumption): Consumption {
        return mongoTemplate.save(consumption)
    }

    override fun insertConsumptions(consumptions: List<Consumption>): Collection<Consumption> {
        return mongoTemplate.insert(consumptions, Consumption::class.java)
    }

    override fun deleteConsumption(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query, Consumption::class.java)
    }

    override fun existsConsumptionById(id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.exists(query, Consumption::class.java)
    }

    override fun belongsConsumptionToUser(id: String, userInfoId: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id).and("userInfoId").`is`(userInfoId))
        return mongoTemplate.exists(query, Consumption::class.java)
    }
}