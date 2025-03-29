package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun ScreenHeader(
    label: String = "",
    onNavigateBackClicked: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onNavigateBackClicked() }
        ) {
            Icon(
                modifier = Modifier
                    .padding(
                        start = 4.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
            )
        }

        Text(
            text = label,
            style = LocalSpendlessTypography.current.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Preview
@Composable
private fun ScreenHeaderPreview() {
    ScreenHeader(label = "Settings")
}