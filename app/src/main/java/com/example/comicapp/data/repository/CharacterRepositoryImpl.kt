package com.example.comicapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.comicapp.data.local.RickAndMortyDatabase
import com.example.comicapp.data.mapper.toCharacter
import com.example.comicapp.data.paging.CharacterRemoteMediator
import com.example.comicapp.data.remote.RickAndMortyApi
import com.example.comicapp.domain.model.Character
import com.example.comicapp.domain.model.Episode
import com.example.comicapp.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val database: RickAndMortyDatabase
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(
        query: String,
        status: String,
        species: String
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharacterRemoteMediator(
                api = api,
                database = database,
                query = query,
                status = status,
                species = species
            ),
            pagingSourceFactory = {
                database.characterDao.getCharacters(query, status, species)
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Character(
                    id = entity.id,
                    name = entity.name,
                    status = entity.status,
                    species = entity.species,
                    type = entity.type,
                    gender = entity.gender,
                    origin = entity.origin,
                    location = entity.location,
                    image = entity.image,
                    episodeUrls = entity.episodeUrls.split(",").filter { it.isNotEmpty() }
                )
            }
        }
    }

    override suspend fun getCharacterById(id: Int): Character? {
        return database.characterDao.getCharacterById(id)?.let { entity ->
            Character(
                id = entity.id,
                name = entity.name,
                status = entity.status,
                species = entity.species,
                type = entity.type,
                gender = entity.gender,
                origin = entity.origin,
                location = entity.location,
                image = entity.image,
                episodeUrls = entity.episodeUrls.split(",").filter { it.isNotEmpty() }
            )
        }
    }

    override suspend fun getEpisodesByIds(ids: String): List<Episode> {
        return try {
            val dtos = api.getEpisodes(ids)
            dtos.map { dto ->
                Episode(
                    id = dto.id,
                    name = dto.name,
                    airDate = dto.airDate,
                    episode = dto.episode,
                    characters = dto.characters,
                    url = dto.url,
                    created = dto.created
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
