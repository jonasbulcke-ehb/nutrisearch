package be.ehb.gdt.nutrisearch.restapi.dietitians

import be.ehb.gdt.nutrisearch.domain.dietitians.exceptions.DietitianRegistrationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class DietitiansRestControllerExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [DietitianRegistrationException::class])
    fun handleDuplicateRegistration(
        exception: DietitianRegistrationException,
        request: WebRequest
    ): ResponseEntity<Any> {
        return handleExceptionInternal(exception, exception.errorCode, HttpHeaders(), HttpStatus.BAD_REQUEST, request)!!
    }

}