package be.ehb.gdt.nutrisearch.domain.dietitians.repositories

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
@SuppressWarnings("kotlin:S6518")
class MongoDietitianRepository(private val mongoTemplate: MongoTemplate) : DietitianRepository {
    override fun findDietitians(): List<Dietitian> = mongoTemplate.findAll(Dietitian::class.java)

    override fun findDietitianById(id: String) = mongoTemplate.findById(id, Dietitian::class.java)

    override fun findDietitianByAuthId(authId: String): Dietitian? {
        val query = Query(Criteria.where("authId").`is`(authId))
        return mongoTemplate.findOne(query, Dietitian::class.java)
    }

    override fun insertDietitian(dietitian: Dietitian) = mongoTemplate.insert(dietitian)
    override fun updateDietitian(id: String, firstname: String, lastname: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("firstname", firstname).set("lastname", lastname)
        mongoTemplate.updateFirst(query, update, Dietitian::class.java)
    }

    override fun deleteDietitian(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query, Dietitian::class.java)
    }

    override fun existsById(id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.exists(query, Dietitian::class.java)
    }

    override fun existsByAuthId(authId: String): Boolean {
        val query = Query(Criteria.where("authId").`is`(authId))
        return mongoTemplate.exists(query, Dietitian::class.java)
    }
}