package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import kotlinx.coroutines.delay

const val MAX_VISIBLE_TIME = 2000L

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    text: String,
    navigationBarSizeDp: Dp = 0.dp,
    onBannerFinished: () -> Unit = {}
) {
    var showBanner by remember { mutableStateOf(true) }
    val imeInsets = WindowInsets.ime.asPaddingValues()
    val isKeyboardOpened = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    LaunchedEffect(Unit) {
        delay(MAX_VISIBLE_TIME)
        showBanner = false
        onBannerFinished()
    }

    AnimatedVisibility(
        modifier = modifier
            .padding(
                bottom = if(isKeyboardOpened) imeInsets.calculateBottomPadding() else 0.dp
            ),
        visible = showBanner,
        content = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = LocalSpendlessColorsPalette.current.error)
                    .padding(
                        top = 12.dp,
                        bottom = if(isKeyboardOpened) 12.dp else navigationBarSizeDp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = text,
                    style = LocalSpendlessTypography.current.labelMedium,
                    color = LocalSpendlessColorsPalette.current.onError,
                    textAlign = TextAlign.Center,
                )
            }
        }
    )
}

@Preview
@Composable
private fun BannerPreview() {
    Banner(text = "Wrong pin")
}