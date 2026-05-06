package com.example.comicapp.domain.repository

import androidx.paging.PagingData
import com.example.comicapp.domain.model.Character
import com.example.comicapp.domain.model.Episode
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(query: String, status: String, species: String): Flow<PagingData<Character>>
    suspend fun getCharacterById(id: Int): Character?
    suspend fun getEpisodesByIds(ids: String): List<Episode>
}
