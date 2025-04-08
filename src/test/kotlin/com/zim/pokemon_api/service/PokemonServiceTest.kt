//package com.zim.pokemon_api.service
//
//import com.zim.pokemon_api.model.Pokemon
//import com.zim.pokemon_api.repository.PokemonRepository
//import org.junit.jupiter.api.Test
//import org.mockito.kotlin.*
//import org.assertj.core.api.Assertions.assertThat
//
//class PokemonServiceTest {
//    private val pokemonRepository: PokemonRepository = mock()
//    private val pokemonService = PokemonService(pokemonRepository)
//
//    @Test
//    fun `should return all pokemons from the repository`() {
//        // Given
//        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
//        whenever(pokemonRepository.findAll()).thenReturn(listOf(pokemon))
//
//        // When
//        val result = pokemonService.getAll()
//
//        // Then
//        assertThat(result).hasSize(1)
//        val resultPokemon = result[0]
//        assertThat(resultPokemon.id).isEqualTo(1)
//        assertThat(resultPokemon.pokedexNumber).isEqualTo("001")
//        assertThat(resultPokemon.name).isEqualTo("Bulbasaur")
//        assertThat(resultPokemon.img).isEqualTo("img_url")
//        assertThat(resultPokemon.types).containsExactly("Grass", "Poison")
//    }
//
//    @Test
//    fun `should find a pokemon by id`() {
//        // Given
//        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
//        whenever(pokemonRepository.findById(1)).thenReturn(java.util.Optional.of(pokemon))
//
//        // When
//        val result = pokemonService.getPokemonById(1)
//
//        // Then
//        assertThat(result).isNotNull
//        assertThat(result?.id).isEqualTo(1)
//        assertThat(result?.pokedexNumber).isEqualTo("001")
//        assertThat(result?.name).isEqualTo("Bulbasaur")
//        assertThat(result?.img).isEqualTo("img_url")
//        assertThat(result?.types).containsExactly("Grass", "Poison")
//    }
//
//    @Test
//    fun `should return null when pokemon not found by id`() {
//        // Given
//        whenever(pokemonRepository.findById(999)).thenReturn(java.util.Optional.empty())
//
//        // When
//        val result = pokemonService.getPokemonById(999)
//
//        // Then
//        assertThat(result).isNull()
//    }
//
//    @Test
//    fun `should save all pokemons to the repository`() {
//        // Given
//        val pokemon1 = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
//        val pokemon2 = Pokemon(2, "002", "Ivysaur", "img_url2", listOf("Grass", "Poison"))
//        val pokemons = listOf(pokemon1, pokemon2)
//
//        // When
//        pokemonService.saveAll(pokemons)
//
//        // Then
//        verify(pokemonRepository).saveAll(pokemons)
//    }
//}

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

        val pokemonTypes2 = listOf(PokemonType(name = "Fire"))
        val pokemon2 = Pokemon(4, "Charmander", "img_url2", pokemonTypes2)

        whenever(pokemonRepository.findAll()).thenReturn(listOf(pokemon1, pokemon2))

        val result = pokemonService.getAll()

        assertThat(result).hasSize(2)

        val resultPokemon1 = result[0]
        assertThat(resultPokemon1.pokedexNumber).isEqualTo(1)
        assertThat(resultPokemon1.name).isEqualTo("Bulbasaur")
        assertThat(resultPokemon1.img).isEqualTo("img_url")
        assertThat(resultPokemon1.types).containsExactly(
            PokemonType(name = "Grass"),
            PokemonType(name = "Poison")
        )

        val resultPokemon2 = result[1]
        assertThat(resultPokemon2.pokedexNumber).isEqualTo(4)
        assertThat(resultPokemon2.name).isEqualTo("Charmander")
        assertThat(resultPokemon2.img).isEqualTo("img_url2")
        assertThat(resultPokemon2.types).containsExactly(
            PokemonType(name = "Fire")
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
        assertThat(result?.img).isEqualTo("img_url")
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
