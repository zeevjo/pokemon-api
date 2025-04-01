package com.zim.pokemon_api.repository

import com.zim.pokemon_api.model.Pokemon
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.assertj.core.api.Assertions.assertThat

@ExtendWith(SpringExtension::class)
@DataJpaTest
class PokemonRepositoryTest {

    @Autowired
    private lateinit var pokemonRepository: PokemonRepository

    @Test
    fun `should save a Pokemon to the database`() {
        // Given
        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))

        // When
        val savedPokemon = pokemonRepository.save(pokemon)

        // Then
        assertThat(savedPokemon.id).isNotNull()
        assertThat(savedPokemon.name).isEqualTo("Bulbasaur")
    }

    @Test
    fun `should find a Pokemon by ID`() {
        // Given
        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
        pokemonRepository.save(pokemon)

        // When
        val foundPokemon = pokemonRepository.findById(1).get()

        // Then
        assertThat(foundPokemon.name).isEqualTo("Bulbasaur")
    }

    @Test
    fun `should delete a Pokemon from the database`() {
        // Given
        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
        val savedPokemon = pokemonRepository.save(pokemon)

        // When
        pokemonRepository.delete(savedPokemon)

        // Then
        assertThat(pokemonRepository.existsById(savedPokemon.id)).isFalse()
    }
}
