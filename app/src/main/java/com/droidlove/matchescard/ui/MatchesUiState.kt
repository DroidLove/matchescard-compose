package com.droidlove.matchescard.ui

import com.droidlove.matchescard.model.Match

/**
 * The screen is a pure function of this. No `RecyclerView` adapter holding a mutable list, no
 * `notifyItemRemoved(position)` — which is where the original drifted out of sync when a row was
 * removed while an animation was still running.
 */
data class MatchesUiState(
    val phase: Phase = Phase.Loading,
    val matches: List<Match> = emptyList(),
    val errorMessage: String? = null,
) {
    enum class Phase { Loading, Content, Empty, Error }
}
