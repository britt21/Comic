package com.example.comicapp.domain.use_case

import com.example.comicapp.domain.model.Character
import com.example.comicapp.domain.repository.CharacterRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetCharacterUseCaseTest {

    private lateinit var getCharacterUseCase: GetCharacterUseCase

    @Mock
    private lateinit var repository: CharacterRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getCharacterUseCase = GetCharacterUseCase(repository)
    }

    @Test
    fun `invoke should return character from repository`() = runBlocking {
        // Given
        val characterId = 1
        val expectedCharacter = Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = "Earth",
            location = "Earth",
            image = "url",
            episodeUrls = listOf("ep1", "ep2")
        )
        `when`(repository.getCharacterById(characterId)).thenReturn(expectedCharacter)

        // When
        val result = getCharacterUseCase(characterId)

        // Then
        assertEquals(expectedCharacter, result)
    }
}
