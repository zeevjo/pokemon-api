package com.zim.pokemon_api.config

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.zim.pokemon_api.model.Pokemon
import com.zim.pokemon_api.model.PokemonType
import com.zim.pokemon_api.service.PokemonService
import com.zim.pokemon_api.service.PokemonTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import java.net.URI

@Configuration
class DataInitializer {
    @Value("\${spring.poke.path}")
    lateinit var pokePath: String

    @Autowired
    lateinit var pokemonService: PokemonService

    @Autowired
    lateinit var pokemonTypeService: PokemonTypeService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Bean
    fun initDatabase(): CommandLineRunner {
        return CommandLineRunner {
            try {
                val jsonNode = loadPokemonJsonData()
                val pokemonArray = extractPokemonArray(jsonNode)

                val typeNames = collectUniqueTypeNames(pokemonArray)
                savePokemonTypes(typeNames)

                val pokemons = createPokemonEntities(pokemonArray)
                pokemonService.saveAll(pokemons)

                println("Successfully loaded ${pokemons.size} Pokemon into the database")
            } catch (e: IOException) {
                println("Error reading the file: ${e.message}")
                throw RuntimeException("Failed to initialize Pokemon database", e)
            }
        }
    }

    fun loadPokemonJsonData(): JsonNode {
        val resource = ClassPathResource(pokePath)
        return objectMapper.readTree(resource.inputStream.use { it.readBytes() })
    }

    private fun extractPokemonArray(jsonNode: JsonNode): JsonNode {
        return jsonNode.get("pokemon")
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

    private fun savePokemonTypes(typeNames: Set<String>): List<PokemonType> {
        val types = typeNames.map { name -> PokemonType(name = name) }
        pokemonTypeService.saveAll(types)
        return types
    }

    private fun createPokemonEntities(pokemonArray: JsonNode): List<Pokemon> {
        return pokemonArray.map { pokemonNode ->
            val id = pokemonNode.get("id").asInt()
            val name = pokemonNode.get("name").asText()
            val url = URI(pokemonNode.get("img").asText()).toURL()
            val types = extractPokemonTypes(pokemonNode)
            Pokemon(id, name, url, types)
        }
    }

    private fun extractPokemonTypes(pokemonNode: JsonNode): List<PokemonType> {
        return pokemonNode.get("type").map { typeNode ->
            val typeName = typeNode.asText()
            pokemonTypeService.findByName(typeName)
        }
    }
}