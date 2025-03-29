package com.icdominguez.spendless.presentation.screens.unauthorized.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.Banner
import com.icdominguez.spendless.presentation.designsystem.composables.SpendLessButton
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun LoginScreen(
    state: LoginScreenViewModel.State = LoginScreenViewModel.State(),
    uiEvent: (LoginScreenViewModel.Event) -> Unit = {},
    navigateToRegistrationScreen: () -> Unit = {},
    navigateToDashboard: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    
    var isLoginFocused by remember { mutableStateOf(false) }
    var isPinFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 36.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp),
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier
                        .padding(top = 20.dp),
                    text = stringResource(R.string.welcome_back),
                    style = LocalSpendlessTypography.current.headlineMedium,
                )

                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = stringResource(R.string.enter_your_credentials),
                    style = LocalSpendlessTypography.current.bodyMedium,
                )

                BasicTextField(
                    modifier = Modifier
                        .padding(top = 36.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            isLoginFocused = it.isFocused
                        }
                        .then(if(isLoginFocused) Modifier.border(1.dp, LocalSpendlessColorsPalette.current.primary, RoundedCornerShape(16.dp)) else Modifier),
                    textStyle = LocalSpendlessTypography.current.bodyMedium.copy(
                        color = LocalSpendlessColorsPalette.current.onSurface,
                    ),
                    value = state.usernameText,
                    onValueChange = { newValue ->
                        uiEvent(LoginScreenViewModel.Event.OnUsernameTextChange(newValue))
                    },
                    cursorBrush = SolidColor(LocalSpendlessColorsPalette.current.primary),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(10.dp, RoundedCornerShape(16.dp))
                                .background(
                                    color = LocalSpendlessColorsPalette.current.surfaceContainerLowest,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp,
                                )
                        ) {
                            if(state.usernameText.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.username),
                                    style = LocalSpendlessTypography.current.bodyMedium.copy(
                                        color = LocalSpendlessColorsPalette.current.onSurfaceVariant
                                    )
                                )
                            }
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    interactionSource = interactionSource,
                    maxLines = 1,
                )

                BasicTextField(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .onFocusChanged {
                            isPinFocused = it.isFocused
                        }
                        .then(if(isPinFocused) Modifier.border(1.dp, LocalSpendlessColorsPalette.current.primary, RoundedCornerShape(16.dp)) else Modifier),
                    textStyle = LocalSpendlessTypography.current.bodyMedium.copy(
                        color = Color(0xFF1B1B1C)
                    ),
                    value = state.pinText,
                    onValueChange = { newValue ->
                        uiEvent(LoginScreenViewModel.Event.OnPinTextChange(newValue))
                    },
                    cursorBrush = SolidColor(LocalSpendlessColorsPalette.current.primary),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(10.dp, RoundedCornerShape(16.dp))
                                .background(
                                    color = LocalSpendlessColorsPalette.current.surfaceContainerLowest,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 12.dp,
                                )
                        ) {
                            if(state.pinText.isEmpty()) {
                                Text(
                                    text = "PIN",
                                    style = LocalSpendlessTypography.current.bodyMedium.copy(
                                        color = LocalSpendlessColorsPalette.current.onSurfaceVariant
                                    )
                                )
                            }
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    interactionSource = interactionSource,
                    visualTransformation = PasswordVisualTransformation(),
                    maxLines = 1,
                )

                SpendLessButton(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    enabled = state.isLoginButtonEnabled,
                    text = stringResource(R.string.login),
                    onClick = { uiEvent(LoginScreenViewModel.Event.OnLoginButtonClicked) },
                )

                Text(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .clickable(
                            indication = null,
                            onClick = { navigateToRegistrationScreen() },
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    text = stringResource(R.string.new_to_spendless),
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
                    onBannerFinished = { uiEvent(LoginScreenViewModel.Event.OnBannerShown) },
                )
            }

            if(state.shouldNavigate) {
                navigateToDashboard()
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}