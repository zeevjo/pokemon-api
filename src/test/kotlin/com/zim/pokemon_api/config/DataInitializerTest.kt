package com.zim.pokemon_api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.model.PokemonType
import com.zim.pokemon_api.repository.PokemonRepository
import com.zim.pokemon_api.service.PokemonService
import com.zim.pokemon_api.service.PokemonTypeService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class DataInitializerTest {
    @Mock
    lateinit var pokemonService: PokemonService

    @Mock
    lateinit var pokemonTypeService: PokemonTypeService

    @InjectMocks
    lateinit var dataInitializer: DataInitializer

    @Test
    fun `should load and save pokemons from json`() {
        // given
        val objectMapper = ObjectMapper()
        val testResourcePath = "test-pokedex.json"
        dataInitializer.pokePath = testResourcePath

        // Mock the PokemonTypeService to return PokemonType instances when queried by name
        val grassType = PokemonType(name = "Grass")
        val poisonType = PokemonType(name = "Poison")
        whenever(pokemonTypeService.getTypeByName("Grass")).thenReturn(grassType)
        whenever(pokemonTypeService.getTypeByName("Poison")).thenReturn(poisonType)

        // when
        val commandLineRunner = dataInitializer.initDatabase(pokemonService, pokemonTypeService, objectMapper)
        commandLineRunner.run()

        // then
        val captor = argumentCaptor<List<Pokemon>>()
        verify(pokemonService).saveAll(captor.capture())

        val savedPokemons = captor.firstValue
        assertThat(savedPokemons).isNotEmpty()

        assertThat(savedPokemons).hasSize(2)

        val bulbasaur = savedPokemons[0]
        assertThat(bulbasaur.id).isEqualTo(1)
        assertThat(bulbasaur.pokedexNumber).isEqualTo("001")
        assertThat(bulbasaur.name).isEqualTo("Bulbasaur")
        assertThat(bulbasaur.img).isEqualTo("img_url")
        assertThat(bulbasaur.types).containsExactly(
            grassType,
            poisonType
        )

        val ivysaur = savedPokemons[1]
        assertThat(ivysaur.id).isEqualTo(2)
        assertThat(ivysaur.pokedexNumber).isEqualTo("002")
        assertThat(ivysaur.name).isEqualTo("Ivysaur")
        assertThat(ivysaur.img).isEqualTo("img_url")
        assertThat(ivysaur.types).containsExactly(
            grassType,
            poisonType
        )
    }
}
