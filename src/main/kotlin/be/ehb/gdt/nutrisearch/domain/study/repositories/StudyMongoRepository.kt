package be.ehb.gdt.nutrisearch.domain.study.repositories

import be.ehb.gdt.nutrisearch.domain.consumption.entities.Consumption
import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Questionnaire
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.Answer
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.study.valueobjects.UpdatableStudy
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
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

    override fun findParticipantsIdsById(id: String): List<String>? {
        val matchStage = Aggregation.match(Criteria.where("_id").`is`(id))
        val projectStage = Aggregation.project("participants").andExclude("_id")
        return mongoTemplate.aggregate(
            Aggregation.newAggregation(matchStage, projectStage),
            Study::class.java,
            Document::class.java
        ).uniqueMappedResult?.getList("participants", ObjectId::class.java)?.map(ObjectId::toHexString)
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
        val update = Update().push("participants", ObjectId(userInfoId))
        mongoTemplate.updateFirst(query, update, Study::class.java)
    }

    override fun deleteParticipant(studyId: String, userInfoId: String) {
        val query = Query(Criteria.where("_id").`is`(studyId))
        val update = Update().pull("participants", ObjectId(userInfoId))
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

    override fun findConsumptionsByStudyId(id: String, timestamp: LocalDate): List<Consumption> {
        val matchStudyIdStage = Aggregation.match(Criteria.where("_id").`is`(id))
        val lookupStage = Aggregation.lookup("consumptions", "participants", "userInfoId", "consumptions")
        val unwindStage = Aggregation.unwind("consumptions")
        val replaceRootStage = Aggregation.replaceRoot("consumptions")
        val matchTimestampStage = Aggregation.match(Criteria.where("timestamp").`is`(timestamp.toString()))
        return mongoTemplate.aggregate(
            Aggregation.newAggregation(
                matchStudyIdStage,
                lookupStage,
                unwindStage,
                replaceRootStage,
                matchTimestampStage
            ),
            Study::class.java,
            Consumption::class.java
        ).mappedResults
    }

    override fun findAnswersByStudyId(id: String, date: LocalDate): Map<String, List<Answer>> {
        val matchDateStage = Aggregation.match(Criteria.where("_id.date").`is`(date.toString()))
        val projectStage = Aggregation.project("answers").and("_id.userinfoId").`as`("id")
        val unwindStage = Aggregation.unwind("answers")
        val matchStudyIdStage = Aggregation.match(Criteria.where("answers.studyId").`is`(ObjectId(id)))
        val groupStage = Aggregation.group("id").addToSet("answers").`as`("answers")
        return mongoTemplate.aggregate(
            Aggregation.newAggregation(matchDateStage, projectStage, unwindStage, matchStudyIdStage, groupStage),
            Questionnaire::class.java,
            object {
                lateinit var id: String
                lateinit var answers: List<Answer>
            }::class.java
        ).mappedResults.associate { it.id to it.answers }
    }
}
