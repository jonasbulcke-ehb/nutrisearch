package be.ehb.gdt.nutrisearch.domain.questionnaire.repositories

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.ZoneOffset

@SuppressWarnings("kotlin:S6518")
@Repository
class QuestionMongoRepository(private val mongoTemplate: MongoTemplate) : QuestionRepository {
    override fun findAllQuestionsByUserinfoId(id: String, date: LocalDate): List<Question> {
        val studiesCriteria = Criteria().andOperator(
            Criteria.where("participants").`is`(ObjectId(id)),
            Criteria().orOperator(
                Criteria.where("endDate").isNull,
                Criteria.where("endDate").gt(LocalDate.now())
            ),
            Criteria.where("startDate").lte(date)
        )
        val currentStudyId = mongoTemplate.findOne(Query(studiesCriteria), Study::class.java)?.id

        val questionsCriteria = Criteria.where(SUBJECT_ID_KEY).`in`(id, currentStudyId).orOperator(
            Criteria.where("deletedAt").isNull,
            Criteria.where("deletedAt").gt(date.atStartOfDay().toEpochSecond(ZoneOffset.UTC))
        )
        return mongoTemplate.find(Query(questionsCriteria), Question::class.java)
    }

    override fun findAllQuestionsBySubject(subject: Question.Subject, requestDate: LocalDate): List<Question> {
        val criteria = Criteria().andOperator(
            Criteria.where(SUBJECT_ID_KEY).`is`(subject.id).and("subject.type").`is`(subject.type),
            Criteria().orOperator(
                Criteria.where("deletedAt").isNull,
                Criteria.where("deletedAt").gt(requestDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC))
            )
        )

        return Query(criteria).let { mongoTemplate.find(it, Question::class.java) }
    }

    override fun saveQuestion(question: Question) = mongoTemplate.save(question)

    override fun deleteQuestionById(id: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("deletedAt", LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC))
        mongoTemplate.updateFirst(query, update, Question::class.java)
    }

    companion object {
        const val SUBJECT_ID_KEY = "subject._id"
    }
}