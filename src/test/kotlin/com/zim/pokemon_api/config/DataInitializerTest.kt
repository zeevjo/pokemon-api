package com.zim.pokemon_api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.model.PokemonType
import com.zim.pokemon_api.service.PokemonService
import com.zim.pokemon_api.service.PokemonTypeService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class DataInitializerTest {
    @Mock
    lateinit var pokemonService: PokemonService

    @Mock
    lateinit var pokemonTypeService: PokemonTypeService

    @InjectMocks
    lateinit var dataInitializer: DataInitializer

    private lateinit var grassType: PokemonType
    private lateinit var poisonType: PokemonType
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = ObjectMapper()
        dataInitializer.pokePath = "test-pokedex.json"

        grassType = PokemonType(name ="Grass")
        poisonType = PokemonType(name = "Poison")

        setupPokemonTypeMocks()
    }

    @Test
    fun `should load and save pokemons from json`() {
        val commandLineRunner = dataInitializer.initDatabase(pokemonService, pokemonTypeService, objectMapper)
        commandLineRunner.run()

        verifyPokemonTypesSaved()
        verifyPokemonsSaved()
    }

    private fun setupPokemonTypeMocks() {
        `when`(pokemonTypeService.findByName("Grass")).thenReturn(grassType)
        `when`(pokemonTypeService.findByName("Poison")).thenReturn(poisonType)
    }

    private fun verifyPokemonTypesSaved() {
        val typeCaptor = argumentCaptor<List<PokemonType>>()
        verify(pokemonTypeService).saveAll(typeCaptor.capture())

        val savedTypes = typeCaptor.firstValue
        assertThat(savedTypes).isNotEmpty()
        assertThat(savedTypes.map { it.name }).containsExactlyInAnyOrder("Grass", "Poison")
    }

    private fun verifyPokemonsSaved() {
        val pokemonCaptor = argumentCaptor<List<Pokemon>>()
        verify(pokemonService).saveAll(pokemonCaptor.capture())

        val savedPokemons = pokemonCaptor.firstValue
        assertThat(savedPokemons).isNotEmpty()
        assertThat(savedPokemons).hasSize(2)

        verifyBulbasaurData(savedPokemons[0])
        verifyIvysaurData(savedPokemons[1])
    }

    private fun verifyBulbasaurData(bulbasaur: Pokemon) {
        //assertThat(bulbasaur.id).isEqualTo(1)
        assertThat(bulbasaur.pokedexNumber).isEqualTo(1)
        assertThat(bulbasaur.name).isEqualTo("Bulbasaur")
        assertThat(bulbasaur.img).isEqualTo("img_url")
        assertThat(bulbasaur.types).containsExactly(grassType, poisonType)
    }

    private fun verifyIvysaurData(ivysaur: Pokemon) {
        //assertThat(ivysaur.id).isEqualTo(2)
        assertThat(ivysaur.pokedexNumber).isEqualTo(2)
        assertThat(ivysaur.name).isEqualTo("Ivysaur")
        assertThat(ivysaur.img).isEqualTo("img_url")
        assertThat(ivysaur.types).containsExactly(grassType, poisonType)
    }
}