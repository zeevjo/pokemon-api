package com.zim.pokemon_api.service

import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.repository.PokemonRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.assertj.core.api.Assertions.assertThat

class PokemonServiceTest {

    private val pokemonRepository: PokemonRepository = mock()
    private val pokemonService = PokemonService(pokemonRepository)

    @Test
    fun `should return all pokemons from the repository`() {
        // Given
        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
        whenever(pokemonRepository.findAll()).thenReturn(listOf(pokemon))

        // When
        val result = pokemonService.getAllPokemon()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Bulbasaur")
    }

    @Test
    fun `should find a pokemon by id`() {
        // Given
        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
        whenever(pokemonRepository.findById(1)).thenReturn(java.util.Optional.of(pokemon))

        // When
        val result = pokemonService.getPokemonById(1)

        // Then
        assertThat(result?.name).isEqualTo("Bulbasaur")
    }

    @Test
    fun `should save all pokemons to the repository`() {
        // Given
        val pokemon1 = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
        val pokemon2 = Pokemon(2, "002", "Ivysaur", "img_url", listOf("Grass", "Poison"))
        val pokemons = listOf(pokemon1, pokemon2)

        // When
        pokemonService.saveAll(pokemons)

        // Then
        verify(pokemonRepository).saveAll(pokemons)
    }
}
