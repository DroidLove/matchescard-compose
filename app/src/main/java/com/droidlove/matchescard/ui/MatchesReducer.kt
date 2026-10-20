package com.droidlove.matchescard.ui

import com.droidlove.matchescard.model.Match

/**
 * Every way the screen can change, and the single pure function that applies it.
 *
 * Pure on purpose: the interesting behaviour (what happens when the last card is accepted, when a
 * stale callback arrives, when a load fails mid-list) is testable in milliseconds without a
 * device, a clock, or a coroutine dispatcher.
 */
sealed interface MatchesAction {
    data object Loading : MatchesAction
    data class Loaded(val matches: List<Match>) : MatchesAction
    data class Failed(val message: String) : MatchesAction
    data class Accept(val id: String) : MatchesAction
    data class Reject(val id: String) : MatchesAction
    data object Retry : MatchesAction
}

fun reduce(state: MatchesUiState, action: MatchesAction): MatchesUiState = when (action) {
    MatchesAction.Loading, MatchesAction.Retry ->
        state.copy(phase = MatchesUiState.Phase.Loading, errorMessage = null)

    is MatchesAction.Loaded -> MatchesUiState(
        phase = if (action.matches.isEmpty()) MatchesUiState.Phase.Empty else MatchesUiState.Phase.Content,
        matches = action.matches,
    )

    is MatchesAction.Failed -> state.copy(
        phase = MatchesUiState.Phase.Error,
        errorMessage = action.message,
    )

    is MatchesAction.Accept -> state.decided(action.id)
    is MatchesAction.Reject -> state.decided(action.id)
}

/**
 * Removes a profile by id, not by index.
 *
 * `notifyItemRemoved(position)` is correct only if no other change happened in between. A double
 * tap, or a callback from a row that was already removed, removes the wrong person. An unknown id
 * is a no-op, so a stale tap can never destroy a different profile.
 */
private fun MatchesUiState.decided(id: String): MatchesUiState {
    if (matches.none { it.id == id }) return this

    val remaining = matches.filterNot { it.id == id }
    return copy(
        matches = remaining,
        phase = if (remaining.isEmpty()) MatchesUiState.Phase.Empty else MatchesUiState.Phase.Content,
    )
}
