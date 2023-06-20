package be.ehb.gdt.nutrisearch.domain.study.repositories

import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import java.time.LocalDate

@SuppressWarnings("kotlin:S6518")
@Repository
class StudyMongoRepository(private val mongoTemplate: MongoTemplate) : StudyRepository {
    override fun findAllStudies(): List<Study> {
        return Query(Criteria.where("isDeleted").`is`(false)).let {
            mongoTemplate.find(it, Study::class.java)
        }
    }

    override fun findStudy(id: String): Study? {
        return Query(Criteria.where("_id").`is`(id)).let {
            mongoTemplate.findOne(it, Study::class.java)
        }
    }

    override fun findParticipatingStudy(authId: String): Study? {
        val query = Query(Criteria.where("authId").`is`(authId))
        val userInfoId = mongoTemplate.find(query, UserInfo::class.java)
        return Query(Criteria.where("participants").`is`(userInfoId).and("endDate").gt(LocalDate.now())).let {
            mongoTemplate.findOne(it, Study::class.java)
        }
    }

    override fun insertStudy(study: Study) = mongoTemplate.insert(study)

    override fun updateStudy(study: UpdatableStudy) {
        val query = Query(Criteria.where("_id").`is`(study.id))
        val update = Update()
            .set("startDate", study.startDate)
            .set("endDate", study.endDate)
            .set("subject", study.subject)
        mongoTemplate.updateFirst(query, update, Study::class.java)
    }

    override fun updateEndDate(id: String, endDate: LocalDate) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("endDate", endDate)
        mongoTemplate.updateFirst(query, update, Study::class.java)
    }

    override fun addParticipant(studyId: String, userInfoId: String) {
        val query = Query(Criteria.where("_id").`is`(studyId))
        val update = Update().push("participants", userInfoId)
        mongoTemplate.updateFirst(query, update, Study::class.java)
    }

    override fun deleteParticipant(studyId: String, userInfoId: String) {
        val query = Query(Criteria.where("_id").`is`(studyId))
        val update = Update().pull("participants", userInfoId)
        mongoTemplate.updateFirst(query, update, Study::class.java)
    }

    override fun deleteStudy(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("isDeleted", true)
        mongoTemplate.updateFirst(query, update, Study::class.java)
    }

    override fun hardDeleteStudy(id: String) {
        Query(Criteria.where("_id").`is`(id)).also {
            mongoTemplate.remove(it, Study::class.java)
        }
    }

    override fun existsById(id: String): Boolean {
        return Query(Criteria.where("_id").`is`(id)).let {
            mongoTemplate.exists(it, Study::class.java)
        }
    }
}
