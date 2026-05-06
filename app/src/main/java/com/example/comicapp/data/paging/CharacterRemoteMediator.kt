package com.example.comicapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.comicapp.data.local.RickAndMortyDatabase
import com.example.comicapp.data.local.entity.CharacterEntity
import com.example.comicapp.data.local.entity.RemoteKeyEntity
import com.example.comicapp.data.remote.RickAndMortyApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val api: RickAndMortyApi,
    private val database: RickAndMortyDatabase,
    private val query: String,
    private val status: String,
    private val species: String
) : RemoteMediator<Int, CharacterEntity>() {

    override fun toString(): String {
        return "CharacterRemoteMediator(query='$query', status='$status', species='$species')"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = api.getCharacters(
                page = page,
                name = query.ifEmpty { null },
                status = status.ifEmpty { null },
                species = species.ifEmpty { null }
            )

            val endOfPaginationReached = response.results.isEmpty() || response.info.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeyDao.clearAll()
                    database.characterDao.clearAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = response.results.map {
                    RemoteKeyEntity(characterId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeyDao.insertAll(keys)
                database.characterDao.insertAll(response.results.map { dto ->
                    CharacterEntity(
                        id = dto.id,
                        name = dto.name,
                        status = dto.status,
                        species = dto.species,
                        type = dto.type,
                        gender = dto.gender,
                        origin = dto.origin.name,
                        location = dto.location.name,
                        image = dto.image,
                        episodeUrls = dto.episode.joinToString(",")
                    )
                })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                database.remoteKeyDao.getRemoteKeyById(character.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                database.remoteKeyDao.getRemoteKeyById(character.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharacterEntity>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeyDao.getRemoteKeyById(id)
            }
        }
    }
}
