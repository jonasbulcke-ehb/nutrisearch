package be.ehb.gdt.nutrisearch.domain.userinfo.repositories

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@SuppressWarnings("kotlin:S6518")
@Repository
class UserInfoMongoRepository(private val mongoTemplate: MongoTemplate) : UserInfoRepository {
    override fun findUserInfoById(id: String) = mongoTemplate.findById(id, UserInfo::class.java)

    override fun findUserInfoByAuthId(authId: String): UserInfo? {
        val query = Query(Criteria.where("authId").`is`(authId))
        return mongoTemplate.findOne(query, UserInfo::class.java)
    }

    override fun findUserInfoIdByAuthId(authId: String): String? {
        val query = Query(Criteria.where("authId").`is`(authId))
        return mongoTemplate.findOne(query, UserInfo::class.java)?.id
    }

    override fun findCurrentStudyById(id: String): Study? {
        val matchStage = Aggregation.match(Criteria.where("_id").`is`(id))
        return findCurrentStudy(matchStage)
    }

    override fun findCurrentStudyByAuthId(authId: String): Study? {
        val matchStage = Aggregation.match(Criteria.where("authId").`is`(authId))
        return findCurrentStudy(matchStage)
    }

    override fun findTreatmentTeamByAuthId(authId: String): List<Dietitian> {
        val matchStage = Aggregation.match(Criteria.where("authId").`is`(authId))
        val lookupStage = Aggregation.lookup("dietitians", "treatmentTeam", "_id", "treatmentTeam")
        val unwindStage = Aggregation.unwind("treatmentTeam")
        val replaceRootStage = Aggregation.replaceRoot("treatmentTeam")
        return mongoTemplate.aggregate(
            Aggregation.newAggregation(matchStage, lookupStage, unwindStage, replaceRootStage),
            UserInfo::class.java,
            Dietitian::class.java
        ).mappedResults
    }

    override fun findPatientsByDietitianId(id: String): List<UserInfo> {
        val query = Query(Criteria.where("treatmentTeam").`is`(id).and("authId").ne(null))
        return mongoTemplate.find(query, UserInfo::class.java)
    }

    override fun insertIdToTreatmentTeam(authId: String, id: String) {
        val query = Query(Criteria.where("authId").`is`(authId))
        val update = Update().push("treatmentTeam", id)
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    override fun deleteIdFromTreatmentTeam(authId: String, id: String) {
        val query = Query(Criteria.where("authId").`is`(authId))
        val update = Update().pull("treatmentTeam", id)
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    override fun insertUserInfo(userInfo: UserInfo) = mongoTemplate.insert(userInfo)

    override fun updateUserInfo(authId: String, userUpdatableInfo: UserUpdatableInfo) {
        val query = Query(Criteria.where("authId").`is`(authId))
        val update = Update()
        userUpdatableInfo.run {
            dob?.let { update.set("dob", it) }
            activityLevel?.let { update.set("activityLevel", it) }
            length?.let { update.set("length", it) }
            sex?.let { update.set("sex", it) }
            isPregnant?.let { update.set("isPregnant", it) }
            isBreastfeeding?.let { update.set("isBreastfeeding", it) }
        }
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    override fun insertWeight(authId: String, weightMeasurement: WeightMeasurement) {
        val query = Query(Criteria.where("authId").`is`(authId))
        val update = Update().push("weightMeasurements", weightMeasurement)
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    override fun deleteUserInfoByAuthId(authId: String) {
        val query = Query(Criteria.where("authId").`is`(authId))
        val update = Update().unset("authId")
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    override fun hardDeleteUserInfoByAuthId(authId: String) {
        Query(Criteria.where("authId").`is`(authId)).also {
            mongoTemplate.remove(it, UserInfo::class.java)
        }
    }

    override fun existUserInfoById(id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.exists(query, UserInfo::class.java)
    }

    override fun existUserInfoByAuthId(authId: String): Boolean {
        val query = Query(Criteria.where("authId").`is`(authId))
        return mongoTemplate.exists(query, UserInfo::class.java)
    }

    override fun insertFavoriteProduct(authId: String, productId: String) {
        val query = Query(Criteria.where("authId").`is`(authId))
        val update = Update().push("favoriteProductIds", ObjectId(productId))
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    override fun deleteFavoriteProduct(authId: String, productId: String) {
        val query = Query(Criteria.where("authId").`is`(authId))
        val update = Update().pull("favoriteProductIds", ObjectId(productId))
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    private fun findCurrentStudy(matchStage: MatchOperation): Study? {
        val lookupStage = Aggregation.lookup("studies", "_id", "participants", "studies")
        val unwindStage = Aggregation.unwind("studies")
        val replaceRootStage = Aggregation.replaceRoot("studies")
        return mongoTemplate.aggregate(
            Aggregation.newAggregation(matchStage, lookupStage, unwindStage, replaceRootStage),
            UserInfo::class.java,
            Study::class.java
        ).mappedResults.firstOrNull { it.isActive }
    }
}