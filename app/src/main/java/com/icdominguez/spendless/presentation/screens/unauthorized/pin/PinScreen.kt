package com.icdominguez.spendless.presentation.screens.unauthorized.pin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.KeypadComponent
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun PinScreen(
    state: PinScreenViewModel.State = PinScreenViewModel.State(),
    uiEvent: (PinScreenViewModel.Event) -> Unit = {},
    navigateToRepeatPinScreen: () -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        start = 8.dp,
                    )
                    .clickable(
                        indication = null,
                        onClick = { navigateBack() },
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .size(40.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }

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
                    text = stringResource(R.string.create_pin),
                    style = LocalSpendlessTypography.current.headlineMedium,
                )

                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = stringResource(R.string.use_pin_to_log_in),
                    style = LocalSpendlessTypography.current.bodyMedium.copy(
                        color = LocalSpendlessColorsPalette.current.onSurfaceVariant
                    ),
                )

                KeypadComponent(
                    pinWrote = state.numbers,
                    onNumberClicked = {
                        uiEvent(PinScreenViewModel.Event.OnPinNumberClicked(it))
                    },
                    onRemoveButtonClicked = {
                        uiEvent(PinScreenViewModel.Event.OnRemoveButtonClicked)
                    }
                )
            }
        }
    }

    if(state.numbers.size == 5) {
        uiEvent(PinScreenViewModel.Event.OnNavigate)
        navigateToRepeatPinScreen()
    }
}

@Preview
@Composable
private fun PinScreenPreview() {
    PinScreen()
}
