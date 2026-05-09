package com.example.comicapp.presentation.character_list

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.comicapp.MainActivity
import com.example.comicapp.ui.theme.ComicAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CharacterListScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun topAppBar_displaysTitle() {
        // Since we are using MainActivity, it might already set the content.
        // But CharacterListScreen is the start destination.
        // If we want to test it in isolation, we can still use setContent on composeTestRule
        // which will override the activity's content.

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
