package com.zim.pokemon_api.controller

import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.service.PokemonService
import org.mockito.kotlin.whenever
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.context.bean.override.mockito.MockitoBean

@WebMvcTest(PokemonController::class)
class PokemonControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var pokemonService: PokemonService

    @Test
    fun `should return all pokemons`() {
        // Given
        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
        whenever(pokemonService.getAllPokemon()).thenReturn(listOf(pokemon))

        // When & Then
        mockMvc.get("/api/pokemon")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect {
                jsonPath("$[0].id") { value(1) }
                jsonPath("$[0].pokedexNumber") { value("001") }
                jsonPath("$[0].name") { value("Bulbasaur") }
                jsonPath("$[0].img") { value("img_url") }
                jsonPath("$[0].types[0]") { value("Grass") }
                jsonPath("$[0].types[1]") { value("Poison") }
            }
    }

    @Test
    fun `should return pokemon by id`() {
        // Given
        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
        whenever(pokemonService.getPokemonById(1)).thenReturn(pokemon)

        // When & Then
        mockMvc.get("/api/pokemon/1")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect {
                jsonPath("$.id") { value(1) }
                jsonPath("$.pokedexNumber") { value("001") }
                jsonPath("$.name") { value("Bulbasaur") }
                jsonPath("$.img") { value("img_url") }
                jsonPath("$.types[0]") { value("Grass") }
                jsonPath("$.types[1]") { value("Poison") }
            }
    }

    @Test
    fun `should return not found when pokemon does not exist`() {
        // Given
        whenever(pokemonService.getPokemonById(1)).thenReturn(null)

        // When & Then
        mockMvc.get("/api/pokemon/1")
            .andExpect {
                status { isNotFound() }
                content { string("Pokemon with ID 1 not found") }
            }
    }
}