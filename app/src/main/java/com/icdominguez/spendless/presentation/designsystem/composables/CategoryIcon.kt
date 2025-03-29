package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette

@Composable
fun CategoryIcon(
    modifier: Modifier = Modifier,
    text: String,
    size: Dp = 40.dp,
    backgroundColor: Color = LocalSpendlessColorsPalette.current.primaryFixed,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = text
        )
    }
}

@Preview
@Composable
private fun CategoryIconPreview() {
    CategoryIcon(text = "\uD83C\uDF93")
}