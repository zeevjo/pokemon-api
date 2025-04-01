package com.zim.pokemon_api.model

import jakarta.persistence.*

@Entity
@Table(name = "pokemons")
data class Pokemon(
    @Id
    @Column(name = "id")
    val id: Int,

    @Column(name = "pokedex_number")
    val pokedexNumber: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "img")
    val img: String,

    @ElementCollection
    @CollectionTable(name = "pokemon_types", joinColumns = [JoinColumn(name = "pokemon_id")])
    @Column(name = "type")
    val types: List<String>
)
