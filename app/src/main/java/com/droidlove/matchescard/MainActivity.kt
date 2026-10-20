package com.droidlove.matchescard

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidlove.matchescard.ui.MatchesAction
import com.droidlove.matchescard.ui.MatchesScreen
import com.droidlove.matchescard.ui.MatchesUiState
import com.droidlove.matchescard.ui.MatchesViewModel
import com.droidlove.matchescard.ui.theme.MatchesTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MatchesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MatchesTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()

                MatchesScreen(
                    state = state,
                    onAccept = { id -> viewModel.dispatch(MatchesAction.Accept(id)) },
                    onReject = { id -> viewModel.dispatch(MatchesAction.Reject(id)) },
                    onCall = { id -> dial(state, id) },
                    onEmail = { id -> composeEmail(state, id) },
                    onRetry = viewModel::retry,
                )
            }
        }
    }

    /**
     * Dialling and emailing are intents to *another* app, so "no handler installed" is a normal
     * outcome, not an exception. The original called startActivity directly and crashed on
     * tablets without a dialer.
     */
    private fun dial(state: MatchesUiState, id: String) {
        val phone = state.matches.firstOrNull { it.id == id }?.phone ?: return
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Log.w(TAG, "No dialer available")
        }
    }

    private fun composeEmail(state: MatchesUiState, id: String) {
        val email = state.matches.firstOrNull { it.id == id }?.email ?: return
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Log.w(TAG, "No email client available")
        }
    }

    private companion object {
        const val TAG = "MatchesActivity"
    }
}
