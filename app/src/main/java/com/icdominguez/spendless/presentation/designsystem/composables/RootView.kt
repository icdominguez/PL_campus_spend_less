package com.icdominguez.spendless.presentation.designsystem.composables

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun RootView(
    useLightStatusBarColors: Boolean = false,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val window = (view.context as Activity).window

    LaunchedEffect(Unit) {
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = useLightStatusBarColors
    }

    content()
}