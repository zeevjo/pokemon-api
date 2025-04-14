package com.zim.pokemon_api.repository

import com.zim.pokemon_api.model.PokemonType
import org.springframework.data.jpa.repository.JpaRepository

interface PokemonTypeRepository : JpaRepository<PokemonType, Long> {
    fun findByName(name: String): PokemonType
}
