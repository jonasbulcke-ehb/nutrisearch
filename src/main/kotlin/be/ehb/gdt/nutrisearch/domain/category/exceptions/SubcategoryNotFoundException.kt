package be.ehb.gdt.nutrisearch.domain.category.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class SubcategoryNotFoundException(private val parentId: String, private val id: String): RuntimeException() {
    override val message: String
        get() = "Subcategory with parent category id $parentId and id $id could not be found"
}