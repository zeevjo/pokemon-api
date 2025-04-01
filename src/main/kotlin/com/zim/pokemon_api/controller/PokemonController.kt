package com.zim.pokemon_api.controller

import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.service.PokemonService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/pokemon")
class PokemonController(private val pokemonService: PokemonService) {

    @GetMapping
    fun getAllPokemon(): ResponseEntity<List<Pokemon>> {
        return ResponseEntity.ok(pokemonService.getAllPokemon())
    }

    @GetMapping("/{id}")
    fun getPokemonById(@PathVariable id: Int): ResponseEntity<Pokemon> {
        val pokemon = pokemonService.getPokemonById(id)

        return if (pokemon != null) {
            ResponseEntity.ok(pokemon)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}