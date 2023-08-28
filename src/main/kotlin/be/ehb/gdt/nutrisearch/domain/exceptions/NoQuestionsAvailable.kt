package be.ehb.gdt.nutrisearch.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.LocalDate

@ResponseStatus(HttpStatus.NO_CONTENT)
class NoQuestionsAvailable(private val userinfoId: String, private val date: LocalDate) : RuntimeException() {
    override val message: String
        get() = "There are no questions available for user $userinfoId at $date"
}
