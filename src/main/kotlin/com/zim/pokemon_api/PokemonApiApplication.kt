package com.zim.pokemon_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PokemonApiApplication

fun main(args: Array<String>) {
	runApplication<PokemonApiApplication>(*args)
}