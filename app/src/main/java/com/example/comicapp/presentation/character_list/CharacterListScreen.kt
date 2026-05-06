package com.example.comicapp.presentation.character_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.comicapp.domain.model.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedStatus by viewModel.statusFilter.collectAsState()
    val selectedSpecies by viewModel.speciesFilter.collectAsState()

    val statuses = listOf("Alive", "Dead", "unknown")
    val species = listOf("Human", "Alien", "Robot")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick & Morty characters") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search by name...") },
                singleLine = true
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(statuses) { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = {
                            viewModel.onStatusFilterChange(if (selectedStatus == status) "" else status)
                        },
                        label = { Text(status) }
                    )
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(species) { s ->
                    FilterChip(
                        selected = selectedSpecies == s,
                        onClick = {
                            viewModel.onSpeciesFilterChange(if (selectedSpecies == s) "" else s)
                        },
                        label = { Text(s) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = characters.itemCount,
                    key = characters.itemKey { it.id },
                    contentType = characters.itemContentType { "character" }
                ) { index ->
                    val character = characters[index]
                    if (character != null) {
                        CharacterItem(
                            character = character,
                            onClick = { onCharacterClick(character.id) }
                        )
                    }
                }

                when (val state = characters.loadState.append) {
                    is LoadState.Error -> {
                        item {
                            ErrorRetry(
                                message = state.error.localizedMessage ?: "Error loading more",
                                onRetry = { characters.retry() }
                            )
                        }
                    }
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    else -> {}
                }
            }

            if (characters.loadState.refresh is LoadState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            if (characters.loadState.refresh is LoadState.Error) {
                val e = characters.loadState.refresh as LoadState.Error
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ErrorRetry(
                        message = e.error.localizedMessage ?: "Error loading characters",
                        onRetry = { characters.retry() }
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = character.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(text = "${character.species} - ${character.status}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ErrorRetry(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
