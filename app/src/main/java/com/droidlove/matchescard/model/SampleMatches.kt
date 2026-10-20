package com.droidlove.matchescard.model

import java.time.LocalDate

/**
 * Fixture data. The original app called randomuser.me on every launch, which meant the demo
 * could not be tested, could not be screenshotted deterministically, and broke when the API
 * changed its response shape. A fixture is the boring fix; swapping it for a repository call
 * touches one function in the ViewModel and nothing else.
 */
val sampleMatches: List<Match> = listOf(
    Match(
        id = "1",
        title = "Ms",
        firstName = "Aisha",
        lastName = "Menon",
        city = "Mumbai",
        state = "Maharashtra",
        birthDate = LocalDate.of(1993, 3, 18),
        phone = "+91 98200 00001",
        email = "aisha.menon@example.com",
    ),
    Match(
        id = "2",
        title = "Ms",
        firstName = "Priya",
        lastName = "D'Souza",
        city = "Pune",
        state = "Maharashtra",
        birthDate = LocalDate.of(1991, 11, 2),
        phone = "+91 98200 00002",
        email = "priya.dsouza@example.com",
    ),
    Match(
        id = "3",
        firstName = "Rhea",
        lastName = "Iyer",
        city = "Bengaluru",
        state = "Karnataka",
        birthDate = LocalDate.of(1995, 7, 9),
        phone = "+91 98200 00003",
        email = "rhea.iyer@example.com",
    ),
)
