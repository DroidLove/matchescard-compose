package com.droidlove.matchescard.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidlove.matchescard.model.sampleMatches
import com.droidlove.matchescard.ui.theme.MatchesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Behaviour, not pixels.
 *
 * The bug this is written to catch: a list of cards where every "Accept" button reports the id of
 * whichever profile was first bound. That is invisible in a screenshot and obvious in a tap test.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w411dp-h891dp-xhdpi")
class MatchesInteractionTest {

    @get:Rule
    val compose = createComposeRule()

    private var accepted: String? = null
    private var called: String? = null

    private fun render() {
        compose.setContent {
            MatchesTheme {
                MatchesScreen(
                    state = MatchesUiState(
                        phase = MatchesUiState.Phase.Content,
                        matches = sampleMatches,
                    ),
                    onAccept = { accepted = it },
                    onReject = {},
                    onCall = { called = it },
                    onEmail = {},
                    onRetry = {},
                )
            }
        }
    }

    @Test
    fun `accepting the third card reports the third id`() {
        render()

        compose.onAllNodesWithText("Accept")[2].performClick()

        assertEquals("3", accepted)
    }

    @Test
    fun `each card exposes a labelled call action for screen readers`() {
        render()

        compose.onNodeWithContentDescription("Call Ms. Priya D'Souza").assertIsDisplayed()
            .performClick()

        assertEquals("2", called)
    }

    @Test
    fun `the empty state invites a retry instead of showing a blank screen`() {
        compose.setContent {
            MatchesTheme {
                MatchesScreen(
                    state = MatchesUiState(phase = MatchesUiState.Phase.Empty),
                    onAccept = {},
                    onReject = {},
                    onCall = {},
                    onEmail = {},
                    onRetry = {},
                )
            }
        }

        compose.onAllNodesWithText("No more matches")[0].assertIsDisplayed()
        compose.onAllNodesWithText("Try again")[0].assertIsDisplayed()
    }
}
