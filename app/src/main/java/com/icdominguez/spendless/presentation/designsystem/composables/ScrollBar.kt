package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import kotlinx.coroutines.launch

@Composable
fun ScrollBar(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val coroutineScope = rememberCoroutineScope()

    val thumbHeight = 100.dp
    val trackColor = Color.Transparent
    val thumbColor = LocalSpendlessColorsPalette.current.onSurface.copy(alpha = 0.12f)

    Box(
        modifier = modifier
            .width(6.dp)
            .background(trackColor, shape = RoundedCornerShape(4.dp))
            .pointerInput(scrollState) {
                detectVerticalDragGestures { _, dragAmount ->
                    val newScroll =
                        (scrollState.value + dragAmount.toInt()).coerceIn(0, scrollState.maxValue)
                    coroutineScope.launch {
                        scrollState.scrollTo(newScroll)
                    }
                }
            }
    ) {
        Canvas(modifier =
            Modifier.fillMaxHeight()
        ) {
            val proportion = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
            val yOffset = proportion * (size.height - thumbHeight.toPx())

            drawRoundRect(
                color = thumbColor,
                topLeft = Offset(0f, yOffset),
                size = androidx.compose.ui.geometry.Size(4.dp.toPx(), thumbHeight.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(100.dp.toPx(), 100.dp.toPx())
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScrollBarPreview() {
    ScrollBar(scrollState = rememberScrollState())
}