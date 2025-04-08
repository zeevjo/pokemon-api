package com.zim.pokemon_api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.model.PokemonType
import com.zim.pokemon_api.service.PokemonService
import com.zim.pokemon_api.service.PokemonTypeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class DataInitializer {

    @Value("\${spring.poke.path}")
    lateinit var pokePath: String

    @Bean
    fun initDatabase(pokemonService: PokemonService, pokemonTypeService: PokemonTypeService, objectMapper: ObjectMapper): CommandLineRunner {
        return CommandLineRunner {
            try {
                val resource = ClassPathResource(pokePath)
                val jsonNode = objectMapper.readTree(resource.inputStream.use { it.readBytes() })
                val pokemonArray = jsonNode.get("pokemon")


                val typeNames = mutableSetOf<String>()
                pokemonArray.forEach { pokemonNode ->
                    pokemonNode.get("type").forEach { typeNode ->
                        typeNames.add(typeNode.asText())
                    }
                }

                val types = typeNames.map { name -> PokemonType(name = name) }
                pokemonTypeService.saveAll(types)


                val pokemons = pokemonArray.map { pokemonNode ->
                    val id = pokemonNode.get("id").asInt()
                    val pokedexNumber = pokemonNode.get("num").asText()
                    val name = pokemonNode.get("name").asText()
                    val img = pokemonNode.get("img").asText()


                    val types = pokemonNode.get("type").map { typeNode ->
                        val typeName = typeNode.asText()
                        pokemonTypeService.findByName(typeName)
                    }

                    Pokemon(id, pokedexNumber, name, img, types)
                }


                pokemonService.saveAll(pokemons)
                println("Successfully loaded ${pokemons.size} Pokemon into the database")
            } catch (e: IOException) {
                println("Error reading the file: ${e.message}")
            }
        }
    }
}