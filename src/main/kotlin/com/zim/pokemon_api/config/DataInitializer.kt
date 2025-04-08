//package com.zim.pokemon_api.config
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.zim.pokemon_api.model.Pokemon
//import com.zim.pokemon_api.model.PokemonType
//import com.zim.pokemon_api.service.PokemonService
//import com.zim.pokemon_api.service.PokemonTypeService
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.CommandLineRunner
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.io.ClassPathResource
//import java.io.IOException
//
//@Configuration
//class DataInitializer {
//
//    @Value("\${spring.poke.path}")
//    lateinit var pokePath: String
//
//    @Bean
//    fun initDatabase(pokemonService: PokemonService, pokemonTypeService: PokemonTypeService, objectMapper: ObjectMapper): CommandLineRunner {
//        return CommandLineRunner {
//            try {
//                val resource = ClassPathResource(pokePath)
//                val jsonNode = objectMapper.readTree(resource.inputStream.use { it.readBytes() })
//                val pokemonArray = jsonNode.get("pokemon")
//
//
//                val typeNames = mutableSetOf<String>()
//                pokemonArray.forEach { pokemonNode ->
//                    pokemonNode.get("type").forEach { typeNode ->
//                        typeNames.add(typeNode.asText())
//                    }
//                }
//
//                val types = typeNames.map { name -> PokemonType(name = name) }
//                pokemonTypeService.saveAll(types)
//
//
//                val pokemons = pokemonArray.map { pokemonNode ->
//
//                    val id = pokemonNode.get("id").asInt()
//                    val name = pokemonNode.get("name").asText()
//                    val img = pokemonNode.get("img").asText()
//
//
//                    val types = pokemonNode.get("type").map { typeNode ->
//                        val typeName = typeNode.asText()
//                        pokemonTypeService.findByName(typeName)
//                    }
//
//                    Pokemon(id, name, img, types)
//                }
//
//                pokemonService.saveAll(pokemons)
//                println("Successfully loaded ${pokemons.size} Pokemon into the database")
//            } catch (e: IOException) {
//                println("Error reading the file: ${e.message}")
//            }
//        }
//    }
//}

package com.zim.pokemon_api.config

import com.fasterxml.jackson.databind.JsonNode
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
import kotlin.system.exitProcess

@Configuration
class DataInitializer {
    @Value("\${spring.poke.path}")
    lateinit var pokePath: String

    @Bean
    fun initDatabase(
        pokemonService: PokemonService,
        pokemonTypeService: PokemonTypeService,
        objectMapper: ObjectMapper
    ): CommandLineRunner {
        return CommandLineRunner {
            try {
                val jsonNode = loadPokemonJsonData(objectMapper)
                val pokemonArray = extractPokemonArray(jsonNode)

                extractAndSavePokemonTypes(pokemonArray, pokemonTypeService)
                val pokemons = createPokemonEntities(pokemonArray, pokemonTypeService)

                pokemonService.saveAll(pokemons)
                println("Successfully loaded ${pokemons.size} Pokemon into the database")
            } catch (e: IOException) {
                println("Error reading the file: ${e.message}")
                exitProcess(1)
            }
        }
    }

    private fun loadPokemonJsonData(objectMapper: ObjectMapper): JsonNode {
        val resource = ClassPathResource(pokePath)
        return objectMapper.readTree(resource.inputStream.use { it.readBytes() })
    }

    private fun extractPokemonArray(jsonNode: JsonNode): JsonNode {
        return jsonNode.get("pokemon")
    }

    private fun extractAndSavePokemonTypes(
        pokemonArray: JsonNode,
        pokemonTypeService: PokemonTypeService
    ): List<PokemonType> {
        val typeNames = collectUniqueTypeNames(pokemonArray)
        val types = typeNames.map { name -> PokemonType(name = name) }
        pokemonTypeService.saveAll(types)
        return types
    }

    private fun collectUniqueTypeNames(pokemonArray: JsonNode): Set<String> {
        val typeNames = mutableSetOf<String>()
        pokemonArray.forEach { pokemonNode ->
            pokemonNode.get("type").forEach { typeNode ->
                typeNames.add(typeNode.asText())
            }
        }
        return typeNames
    }

    private fun createPokemonEntities(
        pokemonArray: JsonNode,
        pokemonTypeService: PokemonTypeService
    ): List<Pokemon> {
        return pokemonArray.map { pokemonNode ->
            val id = pokemonNode.get("id").asInt()
            val name = pokemonNode.get("name").asText()
            val img = pokemonNode.get("img").asText()
            val types = extractPokemonTypes(pokemonNode, pokemonTypeService)

            Pokemon(id, name, img, types)
        }
    }

    private fun extractPokemonTypes(
        pokemonNode: JsonNode,
        pokemonTypeService: PokemonTypeService
    ): List<PokemonType> {
        return pokemonNode.get("type").map { typeNode ->
            val typeName = typeNode.asText()
            pokemonTypeService.findByName(typeName)
        }
    }
}