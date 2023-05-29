package be.ehb.gdt.nutrisearch.domain.userinfo.repositories

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import org.springframework.data.mongodb.core.MongoTemplate
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
        query.fields().include("_id")
        return mongoTemplate.findOne(query, UserInfo::class.java)?.id
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
            isBreastFeeding?.let { update.set("isBreastFeeding", it) }
        }
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    override fun insertWeight(authId: String, weightMeasurement: WeightMeasurement) {
        val query = Query(Criteria.where("authId").`is`(authId))
        val update = Update().push("weightMeasurements", weightMeasurement)
        mongoTemplate.updateFirst(query, update, UserInfo::class.java)
    }

    override fun existUserInfoById(id: String): Boolean {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.exists(query, UserInfo::class.java)
    }

    override fun existUserInfoByAuthId(authId: String): Boolean {
        val query = Query(Criteria.where("authId").`is`(authId))
        return mongoTemplate.exists(query, UserInfo::class.java)
    }
}