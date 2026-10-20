package com.droidlove.matchescard.ui

import com.droidlove.matchescard.model.Match
import com.droidlove.matchescard.model.sampleMatches
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The whole decision logic of the screen, tested without Android.
 *
 * These are the cases that broke (or would have broken) in the original adapter: the last row,
 * a stale callback, and a failure arriving after content is already on screen.
 */
class MatchesReducerTest {

    private val loaded = MatchesUiState(
        phase = MatchesUiState.Phase.Content,
        matches = sampleMatches,
    )

    @Test
    fun `accepting removes only the accepted profile`() {
        val next = reduce(loaded, MatchesAction.Accept("2"))

        assertEquals(listOf("1", "3"), next.matches.map { it.id })
        assertEquals(MatchesUiState.Phase.Content, next.phase)
    }

    @Test
    fun `accepting the last profile moves to empty, not to a blank content screen`() {
        val single = loaded.copy(matches = listOf(sampleMatches.first()))

        val next = reduce(single, MatchesAction.Accept("1"))

        assertTrue(next.matches.isEmpty())
        assertEquals(MatchesUiState.Phase.Empty, next.phase)
    }

    @Test
    fun `a stale callback for an already removed profile changes nothing`() {
        val afterFirstTap = reduce(loaded, MatchesAction.Accept("2"))

        val afterSecondTap = reduce(afterFirstTap, MatchesAction.Accept("2"))

        // Same instance: a duplicate tap must not produce a state emission at all.
        assertSame(afterFirstTap, afterSecondTap)
        assertEquals(listOf("1", "3"), afterSecondTap.matches.map { it.id })
    }

    @Test
    fun `reject and accept remove the same way`() {
        val rejected = reduce(loaded, MatchesAction.Reject("1"))
        val accepted = reduce(loaded, MatchesAction.Accept("1"))

        assertEquals(accepted, rejected)
    }

    @Test
    fun `loading a new list replaces the old one and clears prior errors`() {
        val failed = loaded.copy(
            phase = MatchesUiState.Phase.Error,
            errorMessage = "timeout",
        )

        val next = reduce(failed, MatchesAction.Loaded(sampleMatches))

        assertEquals(MatchesUiState.Phase.Content, next.phase)
        assertNull(next.errorMessage)
        assertEquals(3, next.matches.size)
    }

    @Test
    fun `an empty response is the empty phase, not content with zero rows`() {
        val next = reduce(loaded, MatchesAction.Loaded(emptyList()))

        assertEquals(MatchesUiState.Phase.Empty, next.phase)
        assertTrue(next.matches.isEmpty())
    }

    @Test
    fun `retry from an error returns to loading and keeps the error text out of the way`() {
        val failed = loaded.copy(phase = MatchesUiState.Phase.Error, errorMessage = "boom")

        val next = reduce(failed, MatchesAction.Retry)

        assertEquals(MatchesUiState.Phase.Loading, next.phase)
        assertNull(next.errorMessage)
    }

    @Test
    fun `display name keeps the apostrophe the original capitalize() destroyed`() {
        val dsouza: Match = sampleMatches.first { it.lastName == "D'Souza" }

        assertEquals("Ms. Priya D'Souza", dsouza.displayName)
        assertTrue(dsouza.initials == "PD")
        assertEquals("Pune, Maharashtra", dsouza.location)
    }
}
