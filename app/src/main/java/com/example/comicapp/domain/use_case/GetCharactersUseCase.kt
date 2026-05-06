package com.example.comicapp.domain.use_case

import androidx.paging.PagingData
import com.example.comicapp.domain.model.Character
import com.example.comicapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(
        query: String = "",
        status: String = "",
        species: String = ""
    ): Flow<PagingData<Character>> {
        return repository.getCharacters(query, status, species)
    }
}
