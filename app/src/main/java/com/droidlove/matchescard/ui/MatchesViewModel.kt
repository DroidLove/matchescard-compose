package com.droidlove.matchescard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidlove.matchescard.model.sampleMatches
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Thin shell around the reducer: holds state, runs effects, forwards actions.
 *
 * The 2017 app wired an MVP presenter to a view and pushed a `Flowable<List<..>>` into an adapter.
 * Three objects, two of which existed to move data from one to the next. Here the ViewModel owns
 * a `StateFlow` and the screen reads it; the reducer does the only thing that had logic in it.
 */
class MatchesViewModel : ViewModel() {

    private val _state = MutableStateFlow(MatchesUiState())
    val state: StateFlow<MatchesUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun dispatch(action: MatchesAction) {
        _state.value = reduce(_state.value, action)
    }

    fun retry() {
        dispatch(MatchesAction.Retry)
        load()
    }

    private fun load() {
        viewModelScope.launch {
            // Stands in for the repository call. Kept visibly fake so nobody mistakes the demo
            // for a network client.
            delay(400)
            dispatch(MatchesAction.Loaded(sampleMatches))
        }
    }
}
