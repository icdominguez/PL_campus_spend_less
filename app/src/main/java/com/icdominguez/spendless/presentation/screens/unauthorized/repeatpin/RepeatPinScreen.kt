package com.icdominguez.spendless.presentation.screens.unauthorized.repeatpin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.Banner
import com.icdominguez.spendless.presentation.designsystem.composables.KeypadComponent
import com.icdominguez.spendless.presentation.designsystem.composables.ScreenHeader
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun RepeatPinScreen(
    state: RepeatPinScreenViewModel.State = RepeatPinScreenViewModel.State(),
    uiEvent: (RepeatPinScreenViewModel.Event) -> Unit = {},
    navigateToPreferences: () -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            ScreenHeader(
                onNavigateBackClicked = {
                    navigateBack()
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 36.dp)
                        .size(64.dp),
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .padding(top = 20.dp),
                    text = stringResource(R.string.repeat_pin),
                    style = LocalSpendlessTypography.current.headlineMedium,
                )

                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = stringResource(R.string.enter_pin_again),
                    style = LocalSpendlessTypography.current.bodyMedium.copy(
                        color = LocalSpendlessColorsPalette.current.onSurfaceVariant
                    ),
                )

                KeypadComponent(
                    pinWrote = state.numbers,
                    onNumberClicked = {
                        uiEvent(RepeatPinScreenViewModel.Event.OnPinNumberClicked(it))
                    },
                    onRemoveButtonClicked = {
                        uiEvent(RepeatPinScreenViewModel.Event.OnRemoveButtonClicked)
                    }
                )
            }

            if(state.isError) {
                Banner(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    text = state.errorMessage,
                    navigationBarSizeDp = paddingValues.calculateBottomPadding(),
                    onBannerFinished = {
                        uiEvent(RepeatPinScreenViewModel.Event.OnPinEntered)
                    }
                )
            }
        }
    }

    if(state.pinsMatch) {
        uiEvent(RepeatPinScreenViewModel.Event.OnNavigate)
        navigateToPreferences()
    }
}

@Preview
@Composable
private fun RepeatPinScreenPreview() {
    RepeatPinScreen()
}