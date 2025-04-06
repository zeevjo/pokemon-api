package com.zim.pokemon_api.controller

import com.zim.pokemon_api.exception.PokemonNotFoundException
import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.service.PokemonService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/pokemon")
class PokemonController(private val pokemonService: PokemonService) {

    @GetMapping
    fun getAllPokemon(): List<Pokemon> {
        return pokemonService.getAllPokemon()
    }

    @GetMapping("/{id}")
    fun getPokemonById(@PathVariable id: Int): Pokemon {
        return pokemonService.getPokemonById(id)
            ?: throw PokemonNotFoundException("Pokemon with ID $id not found")
    }
}