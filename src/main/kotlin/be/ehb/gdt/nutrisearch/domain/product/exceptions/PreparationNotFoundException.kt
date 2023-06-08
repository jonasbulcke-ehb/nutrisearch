package be.ehb.gdt.nutrisearch.domain.product.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class PreparationNotFoundException(private val productId: String, private val preparationId: String) :
    RuntimeException() {
    override val message: String
        get() = "Preparation with id $preparationId and product id $productId could not be found"
}
