package com.droidlove.matchescard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidlove.matchescard.R
import com.droidlove.matchescard.model.sampleMatches
import com.droidlove.matchescard.ui.theme.MatchesTheme

/**
 * The screen. Takes state, emits intents, owns nothing.
 *
 * The four phases are exhaustive on purpose: `Loading`, `Content`, `Empty` and `Error` are
 * distinct states, not a boolean plus two nullable fields. The original had a `progressBar`
 * visibility flag and a `linearLayoutContent` flag, which allowed the combination "empty list
 * visible while loading" — a real bug that no reviewer notices until a user does.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    state: MatchesUiState,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit,
    onCall: (String) -> Unit,
    onEmail: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (state.phase) {
                MatchesUiState.Phase.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )

                MatchesUiState.Phase.Empty -> MessageState(
                    title = stringResource(R.string.state_empty_title),
                    body = stringResource(R.string.state_empty_body),
                    actionLabel = stringResource(R.string.action_retry),
                    onAction = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )

                MatchesUiState.Phase.Error -> MessageState(
                    title = stringResource(R.string.state_error_title),
                    body = state.errorMessage ?: stringResource(R.string.state_error_body),
                    actionLabel = stringResource(R.string.action_retry),
                    onAction = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )

                MatchesUiState.Phase.Content -> LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(items = state.matches, key = { it.id }) { match ->
                        MatchCard(
                            match = match,
                            onAccept = { onAccept(match.id) },
                            onReject = { onReject(match.id) },
                            onCall = { onCall(match.id) },
                            onEmail = { onEmail(match.id) },
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageState(
    title: String,
    body: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAction) {
            Text(actionLabel)
        }
    }
}

@Preview(name = "Matches", showBackground = true)
@Composable
private fun MatchesScreenPreview() {
    MatchesTheme {
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
