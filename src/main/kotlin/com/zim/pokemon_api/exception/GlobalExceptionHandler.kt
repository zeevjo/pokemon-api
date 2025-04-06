package com.zim.pokemon_api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(PokemonNotFoundException::class)
    fun handlePokemonNotFoundException(ex: PokemonNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }
}