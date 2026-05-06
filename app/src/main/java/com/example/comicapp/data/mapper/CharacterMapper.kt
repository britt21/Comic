package com.example.comicapp.data.mapper

import com.example.comicapp.data.remote.dto.CharacterDto
import com.example.comicapp.domain.model.Character

fun CharacterDto.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin.name,
        location = location.name,
        image = image,
        episodeUrls = episode
    )
}
