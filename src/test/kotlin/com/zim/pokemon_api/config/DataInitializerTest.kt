package com.zim.pokemon_api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
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
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy
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

    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        dataInitializer.pokePath = "test-pokedex.json"

        grassType = PokemonType(name = "Grass")
        poisonType = PokemonType(name = "Poison")

        `when`(pokemonTypeService.findByName("Grass")).thenReturn(grassType)
        `when`(pokemonTypeService.findByName("Poison")).thenReturn(poisonType)
    }

    @Test
    fun `should load and save pokemons from json`() {
        val dataInitializerSpy = spy(dataInitializer)

        val jsonNode = createTestJsonData()
        doReturn(jsonNode).`when`(dataInitializerSpy).loadPokemonJsonData()

        val commandLineRunner = dataInitializerSpy.initDatabase()
        commandLineRunner.run()

        verifyPokemonTypesSaved()
        verifyPokemonsSaved()
    }

    private fun createTestJsonData(): ObjectNode {
        val rootNode = objectMapper.createObjectNode()

        val pokemonArray = objectMapper.createArrayNode()

        val bulbasaur = objectMapper.createObjectNode()
        bulbasaur.put("id", 1)
        bulbasaur.put("name", "Bulbasaur")
        bulbasaur.put("img", "img_url")
        val bulbasaurTypes = objectMapper.createArrayNode()
        bulbasaurTypes.add("Grass")
        bulbasaurTypes.add("Poison")
        bulbasaur.set<ArrayNode>("type", bulbasaurTypes)

        val ivysaur = objectMapper.createObjectNode()
        ivysaur.put("id", 2)
        ivysaur.put("name", "Ivysaur")
        ivysaur.put("img", "img_url")
        val ivysaurTypes = objectMapper.createArrayNode()
        ivysaurTypes.add("Grass")
        ivysaurTypes.add("Poison")
        ivysaur.set<ArrayNode>("type", ivysaurTypes)

        pokemonArray.add(bulbasaur)
        pokemonArray.add(ivysaur)

        rootNode.set<ArrayNode>("pokemon", pokemonArray)

        return rootNode
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
        assertThat(bulbasaur.pokedexNumber).isEqualTo(1)
        assertThat(bulbasaur.name).isEqualTo("Bulbasaur")
        assertThat(bulbasaur.imgUrl).isEqualTo("img_url")
        assertThat(bulbasaur.types).containsExactly(grassType, poisonType)
    }

    private fun verifyIvysaurData(ivysaur: Pokemon) {
        assertThat(ivysaur.pokedexNumber).isEqualTo(2)
        assertThat(ivysaur.name).isEqualTo("Ivysaur")
        assertThat(ivysaur.imgUrl).isEqualTo("img_url")
        assertThat(ivysaur.types).containsExactly(grassType, poisonType)
    }
}