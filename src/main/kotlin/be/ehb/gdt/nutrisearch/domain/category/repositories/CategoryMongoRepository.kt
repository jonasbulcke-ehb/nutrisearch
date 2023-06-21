package be.ehb.gdt.nutrisearch.domain.category.repositories

import be.ehb.gdt.nutrisearch.domain.category.entities.Category
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@SuppressWarnings("kotlin:S6518")
@Repository
class CategoryMongoRepository(private val mongoTemplate: MongoTemplate) : CategoryRepository {
    override fun findAllCategories(): List<Category> = mongoTemplate.findAll(Category::class.java)

    override fun findCategory(id: String) = mongoTemplate.findById(id, Category::class.java)
    override fun saveCategory(category: Category) = mongoTemplate.save(category)

    override fun insertCategory(category: Category) = mongoTemplate.insert(category)

    override fun updateCategory(id: String, name: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("name", name)
        mongoTemplate.updateFirst(query, update, Category::class.java)
    }

    override fun deleteCategory(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query, Category::class.java)
    }

    override fun existsCategoryById(id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.exists(query, Category::class.java)
    }
}
