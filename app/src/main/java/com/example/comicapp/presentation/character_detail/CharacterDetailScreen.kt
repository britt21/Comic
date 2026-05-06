package com.example.comicapp.presentation.character_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.comicapp.domain.model.Episode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.character?.name ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            state.character?.let { character ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    item {
                        AsyncImage(
                            model = character.image,
                            contentDescription = character.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Status: ${character.status}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Species: ${character.species}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Gender: ${character.gender}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Origin: ${character.origin}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Location: ${character.location}", style = MaterialTheme.typography.bodyLarge)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "First 3 Episodes",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    items(state.episodes) { episode ->
                        EpisodeItem(episode = episode)
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(episode: Episode) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = episode.name, fontWeight = FontWeight.Bold)
            Text(text = "${episode.episode} | ${episode.airDate}")
        }
    }
}
