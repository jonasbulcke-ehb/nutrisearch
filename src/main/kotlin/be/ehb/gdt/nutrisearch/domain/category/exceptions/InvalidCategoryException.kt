package be.ehb.gdt.nutrisearch.domain.category.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidCategoryException : RuntimeException() {
    override val message: String
        get() = "Category needs to have at least one subcategory"
}
