package com.droidlove.matchescard.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidlove.matchescard.model.sampleMatches
import com.droidlove.matchescard.ui.theme.MatchesTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Renders the real Compose tree on the JVM with Robolectric's native graphics.
 *
 * Record:  ./gradlew :app:recordRoborazziDebug
 * Compare: ./gradlew :app:verifyRoborazziDebug
 *
 * Determinism: fixed qualifiers, no network, no clock, fixed locale for the date format. A golden
 * that depends on the host is a flaky test that teaches people to re-record instead of read.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w411dp-h891dp-xhdpi")
class MatchesScreenshotTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun matches_list() {
        capture("matches_list") {
            MatchesScreen(
                state = MatchesUiState(
                    phase = MatchesUiState.Phase.Content,
                    matches = sampleMatches,
                ),
                onAccept = {},
                onReject = {},
                onCall = {},
                onEmail = {},
                onRetry = {},
            )
        }
    }

    @Test
    fun matches_empty() {
        capture("matches_empty") {
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

    @Test
    fun matches_error() {
        capture("matches_error") {
            MatchesScreen(
                state = MatchesUiState(
                    phase = MatchesUiState.Phase.Error,
                    errorMessage = "No connection. Check your network.",
                ),
                onAccept = {},
                onReject = {},
                onCall = {},
                onEmail = {},
                onRetry = {},
            )
        }
    }

    private fun capture(fileName: String, content: @Composable () -> Unit) {
        compose.setContent {
            MatchesTheme(darkTheme = false) {
                content()
            }
        }
        compose.onRoot().captureRoboImage("src/test/screenshots/$fileName.png")
    }
}
