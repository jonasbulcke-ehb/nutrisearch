package be.ehb.gdt.nutrisearch.restapi.config

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<Unit> {
        return ResponseEntity.badRequest().build()
    }
}