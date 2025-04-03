//package com.zim.pokemon_api.config
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.zim.pokemon_api.model.Pokemon
//import com.zim.pokemon_api.service.PokemonService
//import org.springframework.boot.CommandLineRunner
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.io.ClassPathResource
//import java.io.IOException
//
//@Configuration
//class DataInitializer {
//
//    @Bean
//    fun initDatabase(pokemonService: PokemonService, objectMapper: ObjectMapper): CommandLineRunner {
//        return CommandLineRunner {
//            try {
//                // Loading resource
//                val resource = ClassPathResource("pokedex.json")
//                val jsonNode = objectMapper.readTree(resource.inputStream.use { it.readBytes() })
//                val pokemonArray = jsonNode.get("pokemon")
//
//                // Converting to Kotlin array
//                val pokemons = pokemonArray.map { pokemonNode ->
//                    val id = pokemonNode.get("id").asInt()
//                    val pokedexNumber = pokemonNode.get("num").asText()
//                    val name = pokemonNode.get("name").asText()
//                    val img = pokemonNode.get("img").asText()
//
//                    // Convert 'type' array to a List<String>
//                    val types = pokemonNode.get("type").map { it.asText() }
//                    Pokemon(id, pokedexNumber, name, img, types)
//                }
//
//                // Save all pokemons to H2DB
//                pokemonService.saveAll(pokemons)
//                println("Successfully loaded ${pokemons.size} Pokemon into the database")
//            } catch (e: IOException) {
//                println("Error reading the file: ${e.message}")
//            }
//        }
//    }
//}


package com.zim.pokemon_api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.service.PokemonService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class DataInitializer {

    @Value("\${poke.path:pokedex.json}")
    lateinit var pokePath: String

    @Bean
    fun initDatabase(pokemonService: PokemonService, objectMapper: ObjectMapper): CommandLineRunner {
        return CommandLineRunner {
            try {
                // Loading resource
                val resource = ClassPathResource(pokePath)
                val jsonNode = objectMapper.readTree(resource.inputStream.use { it.readBytes() })
                val pokemonArray = jsonNode.get("pokemon")

                // Converting to Kotlin array
                val pokemons = pokemonArray.map { pokemonNode ->
                    val id = pokemonNode.get("id").asInt()
                    val pokedexNumber = pokemonNode.get("num").asText()
                    val name = pokemonNode.get("name").asText()
                    val img = pokemonNode.get("img").asText()

                    // Convert 'type' array to a List<String>
                    val types = pokemonNode.get("type").map { it.asText() }
                    Pokemon(id, pokedexNumber, name, img, types)
                }

                // Save all pokemons to H2DB
                pokemonService.saveAll(pokemons)
                println("Successfully loaded ${pokemons.size} Pokemon into the database")
            } catch (e: IOException) {
                println("Error reading the file: ${e.message}")
            }
        }
    }
}
