package com.zim.pokemon_api.repository

import com.zim.pokemon_api.model.Pokemon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PokemonRepository : JpaRepository<Pokemon, Int>