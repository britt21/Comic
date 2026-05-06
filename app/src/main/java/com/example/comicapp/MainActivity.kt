package com.example.comicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.comicapp.presentation.character_detail.CharacterDetailScreen
import com.example.comicapp.presentation.character_list.CharacterListScreen
import com.example.comicapp.ui.theme.ComicAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComicAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "character_list"
                    ) {
                        composable("character_list") {
                            CharacterListScreen(
                                onCharacterClick = { characterId ->
                                    navController.navigate("character_detail/$characterId")
                                }
                            )
                        }
                        composable(
                            route = "character_detail/{characterId}",
                            arguments = listOf(
                                navArgument("characterId") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            CharacterDetailScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
