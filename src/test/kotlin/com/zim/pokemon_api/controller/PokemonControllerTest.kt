package com.zim.pokemon_api.controller

import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.model.PokemonType
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
    fun `should get all pokemons`() {

        val pokemonTypes1 = listOf(PokemonType(name = "Grass"), PokemonType(name = "Poison"))
        val pokemon1 = Pokemon(1, "Bulbasaur", "img_url", pokemonTypes1)

        val pokemonTypes2 = listOf(PokemonType(name = "Fire"))
        val pokemon2 = Pokemon(4, "Charmander", "img_url2", pokemonTypes2)

        whenever(pokemonService.getAll()).thenReturn(listOf(pokemon1, pokemon2))

        mockMvc.get("/api/pokemon")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect {
                jsonPath("$[0].pokedexNumber") { value(1) }
                jsonPath("$[0].name") { value("Bulbasaur") }
                jsonPath("$[0].img") { value("img_url") }
                jsonPath("$[0].types[0].name") { value("Grass") }
                jsonPath("$[0].types[1].name") { value("Poison") }

                jsonPath("$[1].pokedexNumber") { value(4) }
                jsonPath("$[1].name") { value("Charmander") }
                jsonPath("$[1].img") { value("img_url2") }
                jsonPath("$[1].types[0].name") { value("Fire") }
            }
    }

    @Test
    fun `should get pokemon by id`() {

        val pokemonTypes = listOf(PokemonType(name = "Grass"), PokemonType(name = "Poison"))
        val pokemon = Pokemon(1, "Bulbasaur", "img_url", pokemonTypes)
        whenever(pokemonService.getById(1)).thenReturn(pokemon)

        mockMvc.get("/api/pokemon/1")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect {
                jsonPath("$.pokedexNumber") { value(1) }
                jsonPath("$.name") { value("Bulbasaur") }
                jsonPath("$.img") { value("img_url") }
                jsonPath("$.types[0].name") { value("Grass") }
                jsonPath("$.types[1].name") { value("Poison") }
            }
    }

    @Test
    fun `should return not found when pokemon does not exist`() {
        whenever(pokemonService.getById(1)).thenReturn(null)

        mockMvc.get("/api/pokemon/1")
            .andExpect {
                status { isNotFound() }
                content { string("Pokemon with ID 1 not found") }
            }
    }
}