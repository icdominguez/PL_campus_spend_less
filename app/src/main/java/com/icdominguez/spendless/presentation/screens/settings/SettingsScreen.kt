package com.icdominguez.spendless.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.ScreenHeader
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun SettingsScreen(
    state: SettingsScreenViewModel.State = SettingsScreenViewModel.State(),
    uiEvent: (SettingsScreenViewModel.Event) -> Unit = {},
    navigateToPreferencesScreen: () -> Unit = {},
    navigateToSecurityScreen: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            ScreenHeader(
                label = stringResource(R.string.settings_screen),
                onNavigateBackClicked = { navigateBack() }
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = LocalSpendlessColorsPalette.current.surfaceContainerLowest,
                            shape = RoundedCornerShape(16.dp),
                        )
                ) {
                    SettingsItem(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        description = stringResource(R.string.settings_screen_preferences),
                        imageVector = Icons.Outlined.Settings,
                        onClick = { navigateToPreferencesScreen() }
                    )
                    SettingsItem(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                        description = stringResource(R.string.settings_screen_security),
                        imageVector = Icons.Default.Lock,
                        onClick = { navigateToSecurityScreen() }
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = LocalSpendlessColorsPalette.current.surfaceContainerLowest,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    SettingsItem(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(16.dp)),
                        description = stringResource(R.string.settings_screen_log_out),
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        backgroundColor = LocalSpendlessColorsPalette.current.error.copy(alpha = 0.08f),
                        containerColor = LocalSpendlessColorsPalette.current.error,
                        textColor = LocalSpendlessColorsPalette.current.error,
                        onClick = {
                            uiEvent(SettingsScreenViewModel.Event.OnLogOutButtonClicked)
                            navigateToLogin()
                        }
                    )
                }
            }
        }
    }

    if(state.shouldShowAuthenticationScreen) {
        navigateToSecurityScreen()
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    description: String,
    imageVector: ImageVector,
    backgroundColor: Color = LocalSpendlessColorsPalette.current.surfaceContainerLow,
    containerColor: Color = LocalSpendlessColorsPalette.current.onSurfaceVariant,
    textColor: Color = LocalSpendlessColorsPalette.current.onSurface,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClick() }
            )
            .padding(all = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = backgroundColor)
                .padding(10.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = containerColor,
            )
        }
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = description,
            style = LocalSpendlessTypography.current.labelMedium.copy(
                color = textColor,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemPreview() {
    SettingsItem(
        description = "Log out",
        imageVector = Icons.AutoMirrored.Outlined.Logout,
    )
}

