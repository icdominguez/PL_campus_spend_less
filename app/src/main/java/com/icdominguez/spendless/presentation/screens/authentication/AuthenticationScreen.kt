package com.icdominguez.spendless.presentation.screens.authentication

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.core.millisToMinutesSecondsFormat
import com.icdominguez.spendless.presentation.designsystem.composables.Banner
import com.icdominguez.spendless.presentation.designsystem.composables.EnterPinAgainText
import com.icdominguez.spendless.presentation.designsystem.composables.KeypadComponent
import com.icdominguez.spendless.presentation.designsystem.composables.LogOutButton
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography
import com.icdominguez.spendless.presentation.model.LockedOutDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun AuthenticationScreen(
    navigateToLogin: () -> Unit = {},
    onPinEntered: () -> Unit = {},
    state: AuthenticationScreenViewModel.State = AuthenticationScreenViewModel.State(),
    uiEvent: (AuthenticationScreenViewModel.Event) -> Unit = {},
) {
    var currentSecondsPlaying by remember { mutableLongStateOf(0L) }
    var secondsString by remember { mutableStateOf("00:00") }

    LaunchedEffect(state.hasMoreAttempts) {
        if(!state.hasMoreAttempts) {
            currentSecondsPlaying = state.remainingSeconds
                ?: LockedOutDuration.entries.first { it.name == state.user?.lockedOutDuration }.millis
            secondsString = currentSecondsPlaying.millisToMinutesSecondsFormat()
            while (isActive) {
                if(currentSecondsPlaying > 0) {
                    delay(1000)
                    currentSecondsPlaying -= 1000L
                    secondsString = currentSecondsPlaying.millisToMinutesSecondsFormat()
                } else {
                    uiEvent(AuthenticationScreenViewModel.Event.OnCountDownFinished)
                    currentSecondsPlaying = 0L
                    secondsString = "00:00"
                    break
                }
            }
        }
    }

    BackHandler(enabled = true) {  }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                        end = 14.dp
                    )
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                LogOutButton(
                    modifier = Modifier,
                    onLogOut = {
                        navigateToLogin()
                    }
                )
            }

            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 36.dp)
                        .size(64.dp),
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )

                Spacer(modifier = Modifier
                    .height(20.dp))

                state.user?.let { user ->
                    Text(
                        text = if(state.hasMoreAttempts) stringResource(R.string.hello_user, user.username) else stringResource(R.string.too_many_failed_attempts),
                        style = LocalSpendlessTypography.current.headlineMedium
                    )
                }

                Spacer(modifier = Modifier
                    .height(8.dp))

                EnterPinAgainText(
                    hasMoreAttempts = state.hasMoreAttempts,
                    secondsString = secondsString
                )

                Spacer(modifier = Modifier
                    .height(36.dp))

                KeypadComponent(
                    pinWrote = state.numbers,
                    onNumberClicked = { number ->
                        uiEvent(AuthenticationScreenViewModel.Event.OnPinNumberClicked(number))
                    },
                    onRemoveButtonClicked = {
                        uiEvent(AuthenticationScreenViewModel.Event.OnRemoveButtonClicked)
                    },
                    enabled = state.hasMoreAttempts,
                )
            }

            if(state.isError) {
                Banner(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    text = state.errorMessage,
                    navigationBarSizeDp = paddingValues.calculateBottomPadding(),
                    onBannerFinished = {
                        uiEvent(AuthenticationScreenViewModel.Event.OnErrorShown)
                    }
                )
            }

            if(state.numbers.size == 5) {
                uiEvent(AuthenticationScreenViewModel.Event.OnPinEntered)
            }
        }

        if(state.shouldNavigate) {
            onPinEntered()
            uiEvent(AuthenticationScreenViewModel.Event.OnNavigated)
        }
    }
}

@Preview
@Composable
private fun AuthenticationScreenPreview() {
    AuthenticationScreen()
}