package com.zim.pokemon_api.service

import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.repository.PokemonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PokemonService(private val pokemonRepository: PokemonRepository) {

    fun getAll(): List<Pokemon> {
        return pokemonRepository.findAll()
    }

    fun getById(id: Int): Pokemon? {
        return pokemonRepository.findById(id).orElse(null)
    }

    @Transactional
    fun saveAll(pokemons: List<Pokemon>) {
        pokemonRepository.saveAll(pokemons)
    }
}
