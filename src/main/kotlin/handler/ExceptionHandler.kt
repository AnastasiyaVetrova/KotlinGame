package com.project.handler

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<String> {
            return ResponseEntity.status(ex.statusCode).body(ex.message)
    }
}
