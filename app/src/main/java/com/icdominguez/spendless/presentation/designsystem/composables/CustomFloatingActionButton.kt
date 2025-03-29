package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette

@Composable
fun CustomFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .padding(
                bottom = 40.dp,
                end = 24.dp,
            )
            .size(60.dp)
            .shadow(
                shape = RoundedCornerShape(20.dp),
                elevation = 10.dp
            )
            .clip(
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = LocalSpendlessColorsPalette.current.secondaryContainer,
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun CustomFloatingButtonPreview() {
    CustomFloatingActionButton()
}