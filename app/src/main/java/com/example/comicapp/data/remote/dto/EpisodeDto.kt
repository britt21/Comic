package com.example.comicapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EpisodeDto(
    val id: Int,
    val name: String,
    @SerializedName("air_date")
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)
