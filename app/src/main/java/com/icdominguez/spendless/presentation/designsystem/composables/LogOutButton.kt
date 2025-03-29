package com.icdominguez.spendless.presentation.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette

@Composable
fun LogOutButton(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .background(
                color = LocalSpendlessColorsPalette.current.error.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(all = 12.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable(
                onClick = onLogOut,
            )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = null,
            tint = LocalSpendlessColorsPalette.current.error
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LogOutButtonPreview() {
    LogOutButton()
}