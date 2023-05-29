package be.ehb.gdt.nutrisearch.domain.consumption.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ConsumptionNotFoundException(private val id: String) : RuntimeException() {
    override val message: String
        get() = "Consumption with id $id could not be found"
}
