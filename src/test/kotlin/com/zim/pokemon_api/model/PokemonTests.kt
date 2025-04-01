package com.example.pokemon

import com.zim.pokemon_api.model.Pokemon
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class PokemonTests {

    @Test
    fun `should create a Pokemon instance`() {
        val pokemon = Pokemon(
            num = "001",
            name = "Bulbasaur",
            img = "https://www.serebii.net/pokemongo/pokemon/001.png",
            type = listOf("Grass", "Poison")
        )

        assertThat(pokemon.num).isEqualTo("001")
        assertThat(pokemon.name).isEqualTo("Bulbasaur")
        assertThat(pokemon.img).isEqualTo("https://www.serebii.net/pokemongo/pokemon/001.png")
        assertThat(pokemon.type).containsExactly("Grass", "Poison")
    }
}
