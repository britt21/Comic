package com.example.comicapp.domain.use_case

import com.example.comicapp.domain.model.Character
import com.example.comicapp.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Character? {
        return repository.getCharacterById(id)
    }
}
