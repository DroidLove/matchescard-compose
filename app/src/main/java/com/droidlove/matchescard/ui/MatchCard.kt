package com.droidlove.matchescard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidlove.matchescard.R
import com.droidlove.matchescard.model.Match
import com.droidlove.matchescard.model.sampleMatches
import com.droidlove.matchescard.ui.theme.MatchesTheme

/**
 * A single profile card.
 *
 * Stateless: every action is a callback, every value is a parameter. That is what lets the
 * screenshot tests render it with no ViewModel, no network and no coroutines — and what stops a
 * "just call the API here" change from slipping through review.
 *
 * Touch targets: the call and email buttons are 48dp minimum even though the icon is 24dp, and
 * the labels are real text rather than an icon-only button. The original used 38dp Buttons with
 * no content description at all, so TalkBack announced "Button" four times in a row.
 */
@Composable
fun MatchCard(
    match: Match,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onCall: () -> Unit,
    onEmail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {

            Row(verticalAlignment = Alignment.Top) {
                InitialsAvatar(
                    initials = match.initials,
                    modifier = Modifier.size(64.dp),
                )
                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = match.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = match.location,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    match.birthDateLabel?.let { label ->
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onCall) {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = stringResource(R.string.action_call, match.displayName),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onEmail) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = stringResource(R.string.action_email, match.displayName),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onReject) {
                    Text(stringResource(R.string.action_reject))
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = onAccept) {
                    Text(stringResource(R.string.action_accept))
                }
            }
        }
    }
}

/**
 * Avatar fallback: initials on a tinted circle.
 *
 * The original loaded a remote JPEG into a custom `ShapeImageView` with a gender-specific
 * placeholder. That is three failure modes (network, cache, wrong placeholder) for a decoration.
 * When a real image loader is wired in, it replaces this composable and nothing else changes.
 */
@Composable
private fun InitialsAvatar(
    initials: String,
    modifier: Modifier = Modifier,
) {
    val brush = remember(initials) {
        Brush.linearGradient(
            listOf(
                Color(0xFFFFDADB),
                Color(0xFFFFF0F0),
            ),
        )
    }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(brush, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(name = "Match card", showBackground = true, backgroundColor = 0xFFFFF8F8)
@Composable
private fun MatchCardPreview() {
    MatchesTheme {
        MatchCard(
            match = sampleMatches.first(),
            onAccept = {},
            onReject = {},
            onCall = {},
            onEmail = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
