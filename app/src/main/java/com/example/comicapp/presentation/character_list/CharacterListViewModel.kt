package com.example.comicapp.presentation.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.comicapp.domain.model.Character
import com.example.comicapp.domain.use_case.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow("")
    val statusFilter = _statusFilter.asStateFlow()

    private val _speciesFilter = MutableStateFlow("")
    val speciesFilter = _speciesFilter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val characters: StateFlow<PagingData<Character>> = combine(
        _searchQuery.debounce(500),
        _statusFilter,
        _speciesFilter
    ) { query, status, species ->
        Triple(query, status, species)
    }.distinctUntilChanged()
        .flatMapLatest { (query, status, species) ->
            getCharactersUseCase(query, status, species)
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onStatusFilterChange(status: String) {
        _statusFilter.value = status
    }

    fun onSpeciesFilterChange(species: String) {
        _speciesFilter.value = species
    }
}
