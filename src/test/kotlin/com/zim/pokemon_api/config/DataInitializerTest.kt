package com.zim.pokemon_api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.service.PokemonService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class DataInitializerTest {
    @Mock
    lateinit var pokemonService: PokemonService

    @InjectMocks
    lateinit var dataInitializer: DataInitializer

    @Test
    fun `should load and save pokemons from json`() {
        // given
        val objectMapper = ObjectMapper()
        val testResourcePath = "test-pokedex.json"
        dataInitializer.pokePath = testResourcePath

        // when
        val commandLineRunner = dataInitializer.initDatabase(pokemonService, objectMapper)
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
        assertThat(bulbasaur.types).containsExactly("Grass", "Poison")

        val ivysaur = savedPokemons[1]
        assertThat(ivysaur.id).isEqualTo(2)
        assertThat(ivysaur.pokedexNumber).isEqualTo("002")
        assertThat(ivysaur.name).isEqualTo("Ivysaur")
        assertThat(ivysaur.img).isEqualTo("img_url")
        assertThat(ivysaur.types).containsExactly("Grass", "Poison")
    }
}