package com.droidlove.matchescard.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * The 2017 palette, re-derived for contrast.
 *
 * Measured (WCAG 2.1 relative luminance, sRGB):
 *   white on #FF5A60 ... 3.05:1   — the original button pairing; fails AA for normal text
 *   white on #C2182B ... 6.07:1   — the primary used here
 *   #C6A8B2 on white ... 2.18:1   — the original accent; decorative only, never text
 *
 * Keeping #FF5A60 as `primaryContainer` preserves the brand feel without putting white text on a
 * colour that cannot carry it. This is the kind of change that is invisible in a demo and
 * obvious to anyone who has shipped to a market where 30% of users have the font scaled up.
 */

private val BrandRed = Color(0xFFC2182B)
private val BrandRedContainer = Color(0xFFFFDADB)
private val OnBrandRedContainer = Color(0xFF410008)
private val WarmGrey = Color(0xFF77565E)
private val Ink = Color(0xFF201A1B)
private val Paper = Color(0xFFFFF8F8)

private val LightColors = lightColorScheme(
    primary = BrandRed,
    onPrimary = Color.White,
    primaryContainer = BrandRedContainer,
    onPrimaryContainer = OnBrandRedContainer,
    secondary = WarmGrey,
    onSecondary = Color.White,
    background = Paper,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF4DDDE),
    onSurfaceVariant = Color(0xFF534345),
    outlineVariant = Color(0xFFD8C2C4),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB3B6),
    onPrimary = Color(0xFF66001A),
    primaryContainer = Color(0xFF93001F),
    onPrimaryContainer = Color(0xFFFFDADB),
    secondary = Color(0xFFE6BDC5),
    onSecondary = Color(0xFF442930),
    background = Color(0xFF201A1B),
    onBackground = Color(0xFFF1DEDF),
    surface = Color(0xFF201A1B),
    onSurface = Color(0xFFF1DEDF),
    surfaceVariant = Color(0xFF534345),
    onSurfaceVariant = Color(0xFFD8C2C4),
    outlineVariant = Color(0xFF534345),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

private val MatchesShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

@Composable
fun MatchesTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        shapes = MatchesShapes,
        content = content,
    )
}
