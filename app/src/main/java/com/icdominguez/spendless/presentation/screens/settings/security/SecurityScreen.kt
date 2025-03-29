package com.icdominguez.spendless.presentation.screens.settings.security

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.CustomSelector
import com.icdominguez.spendless.presentation.designsystem.composables.ScreenHeader
import com.icdominguez.spendless.presentation.designsystem.composables.SpendLessButton
import com.icdominguez.spendless.presentation.model.LockedOutDuration
import com.icdominguez.spendless.presentation.model.SessionExpiryDuration

@Composable
fun SecurityScreen(
    state: SecurityScreenViewModel.State = SecurityScreenViewModel.State(),
    uiEvent: (SecurityScreenViewModel.Event) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    BackHandler {
        if(!state.shouldShowAuthenticationScreen) {
            navigateBack()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScreenHeader(
                label = stringResource(R.string.security),
                onNavigateBackClicked = { navigateBack() }
            )

            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 24.dp,
                    )
            ) {
                CustomSelector(
                    label = stringResource(R.string.session_expiry_expiration),
                    onItemSelected = { uiEvent(SecurityScreenViewModel.Event.OnSessionExpiryDurationSelected(it)) },
                    selectedItem = state.sessionExpiryDuration.value,
                    items = SessionExpiryDuration.entries.map { it.value }
                )

                CustomSelector(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    label = stringResource(R.string.locked_out_duration),
                    onItemSelected = { uiEvent(SecurityScreenViewModel.Event.OnLockedOutDurationSelected(it)) },
                    selectedItem = state.lockedOutDuration.value,
                    items = LockedOutDuration.entries.map { it.value }
                )
            }

            SpendLessButton(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                    ),
                enabled = true,
                onClick = {
                    uiEvent(SecurityScreenViewModel.Event.OnSaveButtonClicked)
                },
                text = stringResource(R.string.save)
            )
        }
    }

    if(state.shouldShowAuthenticationScreen) {
        /*AuthenticationScreen(
            onPinEntered = {
                uiEvent(SecurityScreenViewModel.Event.OnPinEntered)
                navigateBack()
            }
        )*/
    }
}

@Preview
@Composable
private fun SecurityScreenPreview() {
    SecurityScreen()
}