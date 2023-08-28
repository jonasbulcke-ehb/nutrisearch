package be.ehb.gdt.nutrisearch.domain.questionnaire.repositories

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Questionnaire
import be.ehb.gdt.nutrisearch.domain.questionnaire.valueobjects.Answer
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@SuppressWarnings("kotlin:S6518")
@Repository
class QuestionnaireMongoRepository(val mongoTemplate: MongoTemplate) : QuestionnaireRepository {
    override fun findQuestionnaireById(id: Questionnaire.Identifier): Questionnaire? {
        val query = Query(Criteria.where("_id").`is`(id))
        return mongoTemplate.findOne(query, Questionnaire::class.java)
    }

    override fun saveQuestionnaire(questionnaire: Questionnaire) = mongoTemplate.save(questionnaire)

    override fun insertAnswersToQuestionnaire(id: Questionnaire.Identifier, answers: List<Answer>) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().push("answers").each(answers)
        mongoTemplate.updateFirst(query, update, Questionnaire::class.java)
    }

    override fun insertAnswersToQuestionnaires(ids: List<Questionnaire.Identifier>, answers: List<Answer>) {
        val query = Query(Criteria.where("_id").`in`(ids))
        val update = Update().push("answers").each(answers)
        mongoTemplate.updateMulti(query, update, Questionnaire::class.java)
    }

    override fun updateAnswer(id: Questionnaire.Identifier, answerId: String, answer: String) {
        val query = Query(Criteria.where("_id").`is`(id))
        val update = Update().set("answers.$[element].answer", answer)
            .filterArray(Criteria.where("element._id").`is`(ObjectId(answerId)))
        mongoTemplate.updateFirst(query, update, Questionnaire::class.java)
    }

    override fun deleteAnswersByUserinfoIdAndStudyId(userinfoId: String, studyId: String) {
        val query = Query(Criteria.where("_id.userinfoId").`is`(userinfoId))
        val update = Update().pull("answers", Document("studyId", ObjectId(studyId)))
        mongoTemplate.updateMulti(query, update, Questionnaire::class.java)
    }

    override fun deleteAnswersByQuestionId(questionId: String) {
        val update = Update().pull("answers", Document("question._id", questionId))
        mongoTemplate.updateMulti(Query(), update, Questionnaire::class.java)
    }
}