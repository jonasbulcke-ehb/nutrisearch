package be.ehb.gdt.nutrisearch.domain.product.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ProductNotFoundException(private val identifier: String) : RuntimeException() {
    override val message: String
        get() = "Product with identifier $identifier could not be found"
}
