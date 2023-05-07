package be.ehb.gdt.nutrisearch.domain.category.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class CategoryNotFoundException(private val identifier: String): RuntimeException() {
    override val message: String
        get() = "Category with identifier $identifier could not be found"
}