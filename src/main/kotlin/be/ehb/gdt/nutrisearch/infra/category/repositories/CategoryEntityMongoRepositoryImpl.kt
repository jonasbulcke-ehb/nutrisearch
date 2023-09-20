package be.ehb.gdt.nutrisearch.infra.category.repositories

import be.ehb.gdt.nutrisearch.infra.category.entities.CategoryEntity
import be.ehb.gdt.nutrisearch.infra.category.entities.SubcategoryEntity
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
class CategoryEntityMongoRepositoryImpl(private val mongoTemplate: MongoTemplate) : CategoryEntityRepository {
    override fun findAllCategories(): List<CategoryEntity> = mongoTemplate.findAll(CategoryEntity::class.java)

    override fun findCategoryById(id: String) = mongoTemplate.findById(id, CategoryEntity::class.java)
    override fun insertCategory(category: CategoryEntity) = mongoTemplate.insert(category)
    override fun insertSubcategory(parentId: String, subcategory: SubcategoryEntity): SubcategoryEntity {
        val query = Query(Criteria.where("_id").`is`(parentId))
        val update = Update().push("subcategories", subcategory)
        mongoTemplate.updateFirst(query, update, CategoryEntity::class.java)
        return subcategory
    }

    override fun updateCategory(id: String, name: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("name", name)
        mongoTemplate.updateFirst(query, update, CategoryEntity::class.java)
    }

    override fun updateSubcategory(parentId: String, subcategory: SubcategoryEntity) {
        val query = Query(Criteria.where("_id").`is`(parentId).and(SUBCATEGORY_ID_KEY).`is`(subcategory.id))
        val update = Update().set("subcategories.$.name", subcategory.name)
        mongoTemplate.updateFirst(query, update, CategoryEntity::class.java)
    }

    override fun deleteCategoryById(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query, CategoryEntity::class.java)
    }

    override fun deleteSubcategoryId(parentId: String, id: String) {
        val query = Query(Criteria.where("_id").`is`(parentId))
        val update = Update().pull("subcategories", Query(Criteria.where("_id").`is`(id)))
        mongoTemplate.updateFirst(query, update, CategoryEntity::class.java)
    }

    override fun existsCategory(id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.exists(query, CategoryEntity::class.java)
    }

    override fun existsSubcategory(parentId: String, id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(parentId).and(SUBCATEGORY_ID_KEY).`is`(id))
        return mongoTemplate.exists(query, CategoryEntity::class.java)
    }

    override fun countSubcategories(id: String): Int {
        val matchStage = Aggregation.match(Criteria.where("_id").`is`(id))
        val projectionOperation = ProjectionOperation().andExclude("_id").and("subcategories").size()
        return mongoTemplate.aggregate(
            Aggregation.newAggregation(matchStage, projectionOperation),
            CategoryEntity::class.java,
            Document::class.java
        ).uniqueMappedResult!!.getInteger("subcategories")
    }

    companion object {
        private const val SUBCATEGORY_ID_KEY = "subcategories._id"
    }
}