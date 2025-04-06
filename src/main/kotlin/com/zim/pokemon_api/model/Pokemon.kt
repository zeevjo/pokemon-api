package com.zim.pokemon_api.model

import jakarta.persistence.*

@Entity
@Table(name = "pokemons")
data class Pokemon(
    @Id
    val id: Int,

    val pokedexNumber: String,

    val name: String,

    val img: String,

    @ManyToMany
    @JoinTable(
        name = "pokemon_pokemon_types",
        joinColumns = [JoinColumn(name = "pokemon_id")],
        inverseJoinColumns = [JoinColumn(name = "pokemon_type_id")]
    )
    val types: List<PokemonType> = mutableListOf()
)