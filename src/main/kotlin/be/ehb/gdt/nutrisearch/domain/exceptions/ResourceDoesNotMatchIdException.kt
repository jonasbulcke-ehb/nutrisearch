package be.ehb.gdt.nutrisearch.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ResourceDoesNotMatchIdException(private val resourceId: String, private val id: String): RuntimeException() {
    override val message: String
        get() = "Resource with id \"$resourceId\" does not match with provided id \"$id\""
}