package com.icdominguez.spendless.presentation.screens.unauthorized.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.Banner
import com.icdominguez.spendless.presentation.designsystem.composables.SpendLessButton
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun RegistrationScreen(
    state: RegistrationScreenViewModel.State = RegistrationScreenViewModel.State(),
    uiEvent: (RegistrationScreenViewModel.Event) -> Unit = {},
    navigateToPinScreen: () -> Unit = {},
    navigateBack: () -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 26.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    modifier = Modifier
                        .padding(
                            top = 36.dp
                        )
                        .size(64.dp),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier
                        .padding(
                            top = 20.dp,
                            start = 12.dp,
                            end = 12.dp,
                        )
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.welcome),
                    style = LocalSpendlessTypography.current.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = stringResource(R.string.create_unique_username),
                    style = LocalSpendlessTypography.current.bodyMedium,
                    textAlign = TextAlign.Center
                )

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp),
                    value = state.usernameText,
                    onValueChange = { newValue ->
                        uiEvent(RegistrationScreenViewModel.Event.OnUsernameTextChanged(newValue))
                    },
                    cursorBrush = SolidColor(LocalSpendlessColorsPalette.current.primary),
                    textStyle = LocalSpendlessTypography.current.displayMedium.copy(
                        color = LocalSpendlessColorsPalette.current.onSurface,
                        textAlign = TextAlign.Center,
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFECE5EE),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if(state.usernameText == "") {
                                Text(
                                    text = stringResource(R.string.username),
                                    style = LocalSpendlessTypography.current.displayMedium,
                                    color = Color(0xFF969397)
                                )
                            }
                            innerTextField()
                        }
                    },
                    singleLine = true
                )

                SpendLessButton(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    enabled = state.isNextButtonEnabled,
                    text = stringResource(R.string.next),
                    onClick = { uiEvent(RegistrationScreenViewModel.Event.OnNextButtonClicked) },
                )

                /*Icon(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = if(state.isNextButtonEnabled) LocalSpendlessColorsPalette.current.onPrimary else Color(0xFF969397),
                )*/

                Text(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .clickable(
                            indication = null,
                            onClick = { navigateBack() },
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    text = stringResource(R.string.already_have_an_account),
                    style = LocalSpendlessTypography.current.titleMedium,
                    color = LocalSpendlessColorsPalette.current.primary,
                )
            }

            if(state.isError) {
                Banner(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    text = state.errorMessage,
                    navigationBarSizeDp = paddingValues.calculateBottomPadding(),
                    onBannerFinished = {
                        uiEvent(RegistrationScreenViewModel.Event.OnErrorShown)
                    }
                )
            }
        }

    }

    if(state.shouldNavigate) {
        uiEvent(RegistrationScreenViewModel.Event.OnNavigate)
        navigateToPinScreen()
    }
}

@Preview
@Composable
private fun RegistrationScreenPreview() {
    RegistrationScreen(navigateBack = {})
}