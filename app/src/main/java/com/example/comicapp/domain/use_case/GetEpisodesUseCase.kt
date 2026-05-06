package com.example.comicapp.domain.use_case

import com.example.comicapp.domain.model.Episode
import com.example.comicapp.domain.repository.CharacterRepository
import javax.inject.Inject

class GetEpisodesUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(episodeUrls: List<String>): List<Episode> {
        val ids = episodeUrls.map { it.split("/").last() }.joinToString(",")
        if (ids.isEmpty()) return emptyList()
        return repository.getEpisodesByIds(ids)
    }
}
