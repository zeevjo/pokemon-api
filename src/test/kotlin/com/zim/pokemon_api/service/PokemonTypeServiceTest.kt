package com.zim.pokemon_api.service

import com.zim.pokemon_api.model.PokemonType
import com.zim.pokemon_api.repository.PokemonTypeRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.Assertions.assertEquals

@ExtendWith(MockitoExtension::class)
class PokemonTypeServiceTest {

    @Mock
    private lateinit var repository: PokemonTypeRepository

    @InjectMocks
    private lateinit var service: PokemonTypeService

    @Test
    fun `should save all pokemon types`() {
        val types = listOf(
            PokemonType(1, "Fire"),
            PokemonType(2, "Water")
        )

        service.saveAll(types)

        verify(repository, times(1)).saveAll(types)
    }

    @Test
    fun `should find pokemon type by name`() {
        val typeName = "Electric"
        val expectedType = PokemonType(3, typeName)
        `when`(repository.findByName(typeName)).thenReturn(expectedType)

        val result = service.findByName(typeName)

        assertEquals(expectedType, result)
        verify(repository, times(1)).findByName(typeName)
    }
}