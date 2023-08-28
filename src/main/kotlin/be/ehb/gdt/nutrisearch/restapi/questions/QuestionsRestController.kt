package be.ehb.gdt.nutrisearch.restapi.questions

import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Question
import be.ehb.gdt.nutrisearch.domain.questionnaire.services.QuestionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/{type}/")
class QuestionsRestController(private val service: QuestionService) {

    @GetMapping("/{id}/questions")
    fun getQuestions(@PathVariable type: Question.Subject.Type, @PathVariable id: String) =
        service.getQuestionsBySubject(type, id)

    @PostMapping("/{id}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    fun postQuestion(
        @PathVariable type: Question.Subject.Type,
        @PathVariable id: String,
        @RequestBody question: Question
    ) = service.createQuestion(question, Question.Subject(type, id))

    @DeleteMapping("/{id}/questions/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteQuestion(
        @PathVariable type: Question.Subject.Type,
        @PathVariable id: String,
        @PathVariable questionId: String
    ) = service.deleteQuestionBySubject(type, id, questionId)
}