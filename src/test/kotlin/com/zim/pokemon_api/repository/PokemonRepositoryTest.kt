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
        assertThat(savedPokemon.pokedexNumber).isEqualTo("001")
        assertThat(savedPokemon.name).isEqualTo("Bulbasaur")
        assertThat(savedPokemon.img).isEqualTo("img_url")
        assertThat(savedPokemon.types).containsExactly("Grass", "Poison")
    }

    @Test
    fun `should find a Pokemon by ID`() {
        // Given
        val pokemon = Pokemon(1, "001", "Bulbasaur", "img_url", listOf("Grass", "Poison"))
        pokemonRepository.save(pokemon)

        // When
        val foundPokemon = pokemonRepository.findById(1).get()

        // Then
        assertThat(foundPokemon.id).isEqualTo(1)
        assertThat(foundPokemon.pokedexNumber).isEqualTo("001")
        assertThat(foundPokemon.name).isEqualTo("Bulbasaur")
        assertThat(foundPokemon.img).isEqualTo("img_url")
        assertThat(foundPokemon.types).containsExactly("Grass", "Poison")
    }

    @Test
    fun `should find all Pokemon in the database`() {
        // Given
        val pokemon1 = Pokemon(1, "001", "Bulbasaur", "img_url_1", listOf("Grass", "Poison"))
        val pokemon2 = Pokemon(2, "002", "Ivysaur", "img_url_2", listOf("Grass", "Poison"))
        pokemonRepository.saveAll(listOf(pokemon1, pokemon2))

        // When
        val allPokemon = pokemonRepository.findAll()

        // Then
        assertThat(allPokemon).hasSize(2)

        val foundPokemon1 = allPokemon.find { it.pokedexNumber == "001" }
        assertThat(foundPokemon1).isNotNull
        assertThat(foundPokemon1?.name).isEqualTo("Bulbasaur")
        assertThat(foundPokemon1?.img).isEqualTo("img_url_1")
        assertThat(foundPokemon1?.types).containsExactly("Grass", "Poison")

        val foundPokemon2 = allPokemon.find { it.pokedexNumber == "002" }
        assertThat(foundPokemon2).isNotNull
        assertThat(foundPokemon2?.name).isEqualTo("Ivysaur")
        assertThat(foundPokemon2?.img).isEqualTo("img_url_2")
        assertThat(foundPokemon2?.types).containsExactly("Grass", "Poison")
    }


    @Test
    fun `should save multiple Pokemon to the database`() {
        // Given
        val pokemon1 = Pokemon(1, "001", "Bulbasaur", "img_url_1", listOf("Grass", "Poison"))
        val pokemon2 = Pokemon(2, "002", "Ivysaur", "img_url_2", listOf("Grass", "Poison"))
        val pokemon3 = Pokemon(3, "003", "Venusaur", "img_url_3", listOf("Grass", "Poison"))
        val pokemonList = listOf(pokemon1, pokemon2, pokemon3)

        // When
        val savedPokemonList = pokemonRepository.saveAll(pokemonList)

        // Then
        assertThat(savedPokemonList).hasSize(3)

        val allPokemon = pokemonRepository.findAll()
        assertThat(allPokemon).hasSize(3)
        val foundPokemon1 = allPokemon.find { it.pokedexNumber == "001" }
        assertThat(foundPokemon1).isNotNull
        assertThat(foundPokemon1?.id).isEqualTo(1)
        assertThat(foundPokemon1?.name).isEqualTo("Bulbasaur")
        assertThat(foundPokemon1?.img).isEqualTo("img_url_1")
        assertThat(foundPokemon1?.types).containsExactly("Grass", "Poison")

        val foundPokemon2 = allPokemon.find { it.pokedexNumber == "002" }
        assertThat(foundPokemon2).isNotNull
        assertThat(foundPokemon2?.id).isEqualTo(2)
        assertThat(foundPokemon2?.name).isEqualTo("Ivysaur")
        assertThat(foundPokemon2?.img).isEqualTo("img_url_2")
        assertThat(foundPokemon2?.types).containsExactly("Grass", "Poison")

        val foundPokemon3 = allPokemon.find { it.pokedexNumber == "003" }
        assertThat(foundPokemon3).isNotNull
        assertThat(foundPokemon3?.id).isEqualTo(3)
        assertThat(foundPokemon3?.name).isEqualTo("Venusaur")
        assertThat(foundPokemon3?.img).isEqualTo("img_url_3")
        assertThat(foundPokemon3?.types).containsExactly("Grass", "Poison")
    }

}