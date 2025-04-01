package com.zim.pokemon_api.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PokemonTest {

    @Test
    fun `should create Pokemon object correctly`() {
        // Given
        val id = 1
        val pokedexNumber = "001"
        val name = "Bulbasaur"
        val img = "hhttp://www.serebii.net/pokemongo/pokemon/001.png"
        val types = listOf("grass", "poison")

        // When
        val pokemon = Pokemon(id, pokedexNumber, name, img, types)

        // Then
        assertThat(pokemon.id).isEqualTo(id)
        assertThat(pokemon.pokedexNumber).isEqualTo(pokedexNumber)
        assertThat(pokemon.name).isEqualTo(name)
        assertThat(pokemon.img).isEqualTo(img)
        assertThat(pokemon.types).containsExactlyInAnyOrderElementsOf(types)
    }
}
