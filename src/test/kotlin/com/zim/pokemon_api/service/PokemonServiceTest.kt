package com.zim.pokemon_api.service

import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.model.PokemonType
import com.zim.pokemon_api.repository.PokemonRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.assertj.core.api.Assertions.assertThat

class PokemonServiceTest {
    private val pokemonRepository: PokemonRepository = mock()
    private val pokemonService = PokemonService(pokemonRepository)

    @Test
    fun `should get all pokemons`() {

        val pokemonTypes1 = listOf(PokemonType(name = "Grass"), PokemonType(name = "Poison"))
        val pokemon1 = Pokemon(1, "Bulbasaur", "img_url", pokemonTypes1)

        val pokemonTypes2 = listOf(PokemonType(name = "Grass"), PokemonType(name = "Poison"))
        val pokemon2 = Pokemon(2, "Ivysaur", "img_url", pokemonTypes2)

        whenever(pokemonRepository.findAll()).thenReturn(listOf(pokemon1, pokemon2))

        val result = pokemonService.getAll()

        assertThat(result).hasSize(2)

        val resultPokemon1 = result[0]
        assertThat(resultPokemon1.pokedexNumber).isEqualTo(1)
        assertThat(resultPokemon1.name).isEqualTo("Bulbasaur")
        assertThat(resultPokemon1.imgUrl).isEqualTo("img_url")
        assertThat(resultPokemon1.types).containsExactly(
            PokemonType(name = "Grass"),
            PokemonType(name = "Poison")
        )

        val resultPokemon2 = result[1]
        assertThat(resultPokemon2.pokedexNumber).isEqualTo(2)
        assertThat(resultPokemon2.name).isEqualTo("Ivysaur")
        assertThat(resultPokemon2.imgUrl).isEqualTo("img_url")
        assertThat(resultPokemon2.types).containsExactly(
            PokemonType(name = "Grass"),
            PokemonType(name = "Poison")
        )
    }


    @Test
    fun `should get pokemon by id`() {
        val pokemonTypes = listOf(PokemonType(name = "Grass"), PokemonType(name = "Poison"))
        val pokemon = Pokemon(1, "Bulbasaur", "img_url", pokemonTypes)
        whenever(pokemonRepository.findById(1)).thenReturn(java.util.Optional.of(pokemon))

        val result = pokemonService.getById(1)

        assertThat(result?.pokedexNumber).isEqualTo(1)
        assertThat(result?.name).isEqualTo("Bulbasaur")
        assertThat(result?.imgUrl).isEqualTo("img_url")
        assertThat(result?.types).containsExactly(PokemonType(name = "Grass"), PokemonType(name = "Poison"))
    }

    @Test
    fun `should return null when pokemon not found`() {
        whenever(pokemonRepository.findById(999)).thenReturn(java.util.Optional.empty())

        val result = pokemonService.getById(999)

        assertThat(result).isNull()
    }

    @Test
    fun `should save all pokemons`() {

        val pokemonTypes1 = listOf(PokemonType(name = "Grass"), PokemonType(name = "Poison"))
        val pokemonTypes2 = listOf(PokemonType(name = "Grass"), PokemonType(name = "Poison"))
        val pokemon1 = Pokemon(1, "Bulbasaur", "img_url", pokemonTypes1)
        val pokemon2 = Pokemon(2, "Ivysaur", "img_url2", pokemonTypes2)
        val pokemons = listOf(pokemon1, pokemon2)

        pokemonService.saveAll(pokemons)

        verify(pokemonRepository).saveAll(pokemons)
    }
}