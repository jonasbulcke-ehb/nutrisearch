package be.ehb.gdt.nutrisearch.domain.category.repositories

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import be.ehb.gdt.nutrisearch.domain.category.entities.Subcategory
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@SuppressWarnings("kotlin:S6518")
@Repository
class SubcategoryMongoRepository(private val mongoTemplate: MongoTemplate) : SubcategoryRepository {
    override fun findAllSubcategories(parentId: String): List<Subcategory> =
        mongoTemplate.findById(parentId, Category::class.java)!!.subcategories

    override fun findSubcategory(parentId: String, id: String): Subcategory? {
        return mongoTemplate.findById(parentId, Category::class.java)!!.subcategories.find { it.id == id }
    }

    override fun insertSubcategory(parentId: String, subcategory: Subcategory): Subcategory {
        val query = Query(Criteria.where("_id").`is`(parentId))
        val update = Update().push("subcategories", subcategory)
        mongoTemplate.updateFirst(query, update, Category::class.java)
        return subcategory
    }

    override fun updateSubcategory(parentId: String, subcategory: Subcategory) {
        val query = Query(Criteria.where("_id").`is`(parentId).and(SUBCATEGORY_ID_KEY).`is`(subcategory.id))
        val update = Update().set("subcategories.$.name", subcategory.name)
        mongoTemplate.updateFirst(query, update, Category::class.java)
    }

    override fun deleteSubcategory(parentId: String, id: String) {
        val query = Query(Criteria.where("_id").`is`(parentId))
        val update = Update().pull("subcategories", Query(Criteria.where("_id").`is`(id)))
        mongoTemplate.updateFirst(query, update, Category::class.java)
    }

    override fun existsParentCategoryById(parentId: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(parentId))
        return mongoTemplate.exists(query, Category::class.java)
    }

    override fun existsSubcategoryById(parentId: String, id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(parentId).and(SUBCATEGORY_ID_KEY).`is`(id))
        return mongoTemplate.exists(query, Category::class.java)
    }

    override fun countSubcategoriesByCategoryId(parentId: String): Int {
        val field = "subcategories"
        val matchStage = Aggregation.match(Criteria.where("_id").`is`(parentId))
        val projectionOperation = ProjectionOperation().andExclude("_id").and(field).size()
        return mongoTemplate.aggregate(
            Aggregation.newAggregation(matchStage, projectionOperation),
            Category::class.java,
            Document::class.java
        ).uniqueMappedResult!!.getInteger("subcategories")
    }

    companion object {
        private const val SUBCATEGORY_ID_KEY = "subcategories._id"
    }
}