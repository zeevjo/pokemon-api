package com.zim.pokemon_api.service

import com.zim.pokemon_api.model.PokemonType
import com.zim.pokemon_api.repository.PokemonTypeRepository
import org.springframework.stereotype.Service

@Service
class PokemonTypeService(private val repository: PokemonTypeRepository) {

    fun saveAll(types: List<PokemonType>) {
        repository.saveAll(types)
    }

    fun findByName(name: String): PokemonType {
        return repository.findByName(name)
    }
}