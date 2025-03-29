package com.icdominguez.spendless.presentation.designsystem.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val primary = Color(0xFF5A00C8)
val onPrimary = Color(0xFFFFFFFF)
val primaryContainer = Color(0xFF8138FF)
val primaryFixed = Color(0xFFEADDFF)
val onPrimaryFixed = Color(0xFF24005A)
val onPrimaryFixedVariant = Color(0xFF5900C7)
val inversePrimary = Color(0xFFD2BCFF)

val secondary = Color(0xFF5F6200)
val secondaryContainer = Color(0xFFD2E750)
val onSecondaryContainer = Color(0xFF414300)
val secondaryFixed = Color(0xFFE5EA58)
val secondaryFixedDim = Color(0xFFC9CE3E)
val tertiaryContainer = Color(0xFFC4E0F9)

val error = Color(0xFFA40019)
val onError = Color(0xFFFFFFFF)
val onSuccess = Color(0xFF29AC08)

val surface = Color(0xFFFCF9F9)
val surfaceContainerLowest = Color(0xFFFFFFFF)
val surfaceContainerLow = Color(0xFFF6F3F3)
val surfaceContainer = Color(0xFFF0EDED)
val surfaceContainerHighest = Color(0xFFE4E2E2)
val onSurface = Color(0xFF1B1B1C)
val onSurfaceVariant = Color(0xFF44474B)
val outline = Color(0xFF75777B)

val inverseSurface = Color(0xFF303031)
val inverseOnSurface = Color(0xFFF3F0F0)
val background = Color(0xFFFEF7FF)
val onBackground = Color(0xFF1D1A25)

sealed class SpendLessColorPalette(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val primaryFixed: Color,
    val onPrimaryFixed: Color,
    val onPrimaryFixedVariant: Color,
    val inversePrimary: Color,
    val secondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val secondaryFixed: Color,
    val secondaryFixedDim: Color,
    val tertiaryContainer: Color,
    val error: Color,
    val onError: Color,
    val onSuccess: Color,
    val surface: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHighest: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val background: Color,
    val onBackground: Color,
)

@Stable
internal data object MainColorPalette : SpendLessColorPalette(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    primaryFixed = primaryFixed,
    onPrimaryFixed = onPrimaryFixed,
    onPrimaryFixedVariant = onPrimaryFixedVariant,
    inversePrimary = inversePrimary,
    secondary = secondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    secondaryFixed = secondaryFixed,
    secondaryFixedDim = secondaryFixedDim,
    tertiaryContainer = tertiaryContainer,
    error = error,
    onError = onError,
    onSuccess = onSuccess,
    surface = surface,
    surfaceContainerLowest = surfaceContainerLowest,
    surfaceContainerLow = surfaceContainerLow,
    surfaceContainer = surfaceContainer,
    surfaceContainerHighest = surfaceContainerHighest,
    onSurface = onSurface,
    onSurfaceVariant = onSurfaceVariant,
    outline = outline,
    inverseSurface = inverseSurface,
    inverseOnSurface = inverseOnSurface,
    background = background,
    onBackground = onBackground,
)

val LocalSpendlessColorsPalette: ProvidableCompositionLocal<SpendLessColorPalette> =
    staticCompositionLocalOf { MainColorPalette }