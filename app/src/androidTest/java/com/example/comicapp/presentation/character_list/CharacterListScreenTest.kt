package com.example.comicapp.presentation.character_list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import com.example.comicapp.ui.theme.ComicAppTheme
import org.junit.Rule
import org.junit.Test

class CharacterListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topAppBar_displaysTitle() {
        composeTestRule.setContent {
            ComicAppTheme {
                CharacterListScreen(onCharacterClick = {})
            }
        }

        composeTestRule.onNodeWithText("Rick & Morty characters").assertIsDisplayed()
    }

    @Test
    fun searchTextField_isDisplayed() {
        composeTestRule.setContent {
            ComicAppTheme {
                CharacterListScreen(onCharacterClick = {})
            }
        }

        composeTestRule.onNodeWithText("Search by name...").assertIsDisplayed()
    }
}
