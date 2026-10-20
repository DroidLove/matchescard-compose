package com.droidlove.matchescard.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * One candidate profile.
 *
 * Deliberately a plain data class with no Android dependency: the reducer, the formatting and
 * every test run on the JVM without a device.
 *
 * Names are rendered exactly as the server sends them. The 2017 version called `.capitalize()`
 * on every field, which turns "D'Souza" into "D'souza" and "McDonald" into "Mcdonald". Casing is
 * a property of the data, not of the view — if the API sends lowercase names, fix the API.
 */
data class Match(
    val id: String,
    val firstName: String,
    val lastName: String,
    val city: String,
    val state: String,
    val title: String? = null,
    val birthDate: LocalDate? = null,
    val phone: String = "",
    val email: String = "",
) {

    val displayName: String
        get() = buildString {
            title?.trim()?.takeIf { it.isNotEmpty() }?.let { append(it).append(". ") }
            append(firstName.trim())
            append(' ')
            append(lastName.trim())
        }

    val location: String
        get() = listOf(city, state)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(", ")

    /** Two letters, for the avatar fallback. Derived, never stored. */
    val initials: String
        get() = listOf(firstName, lastName)
            .mapNotNull { it.trim().firstOrNull()?.uppercaseChar() }
            .joinToString("")

    /**
     * Fixed pattern and explicit locale on purpose: a locale-dependent format would make the
     * screenshot goldens depend on the machine's settings.
     */
    val birthDateLabel: String?
        get() = birthDate?.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH))
}
