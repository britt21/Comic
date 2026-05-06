package com.example.comicapp.presentation.character_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comicapp.domain.model.Character
import com.example.comicapp.domain.model.Episode
import com.example.comicapp.domain.use_case.GetCharacterUseCase
import com.example.comicapp.domain.use_case.GetEpisodesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterDetailState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>("characterId")?.let { characterId ->
            getCharacterDetail(characterId)
        }
    }

    private fun getCharacterDetail(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val character = getCharacterUseCase(id)
            if (character != null) {
                _state.value = _state.value.copy(character = character)
                // Fetch first 3 episodes
                val episodeUrls = character.episodeUrls.take(3)
                val episodes = getEpisodesUseCase(episodeUrls)
                _state.value = _state.value.copy(
                    episodes = episodes,
                    isLoading = false
                )
            } else {
                _state.value = _state.value.copy(
                    error = "Character not found",
                    isLoading = false
                )
            }
        }
    }
}

data class CharacterDetailState(
    val character: Character? = null,
    val episodes: List<Episode> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
