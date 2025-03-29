package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun SpendLessButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    text: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(
                color = if(enabled) LocalSpendlessColorsPalette.current.primary else LocalSpendlessColorsPalette.current.outline.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = { onClick() }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 12.dp),
            text = text,
            style = LocalSpendlessTypography.current.titleMedium,
            color = LocalSpendlessColorsPalette.current.onPrimary,
        )
    }
}

@Preview
@Composable
private fun SpendLessButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SpendLessButton(
            enabled = true,
            onClick = {},
            text = "Login",
        )
        SpendLessButton(
            enabled = false,
            onClick = {},
            text = "Login",
        )
    }
}