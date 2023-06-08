package be.ehb.gdt.nutrisearch.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenOperationException(override val message: String): RuntimeException()