package be.ehb.gdt.nutrisearch.restapi.config

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler(value = [IllegalStateException::class, IllegalArgumentException::class])
    fun handleIllegalStateException(e: RuntimeException): ResponseEntity<Unit> {
        return ResponseEntity.badRequest().build()
    }
}