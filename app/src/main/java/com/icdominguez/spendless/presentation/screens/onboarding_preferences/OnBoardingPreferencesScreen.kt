package com.icdominguez.spendless.presentation.screens.onboarding_preferences

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.PreferencesComponentView
import com.icdominguez.spendless.presentation.designsystem.composables.SpendLessButton
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessTypography

@Composable
fun OnBoardingPreferencesScreen(
    state: OnBoardingPreferencesScreenViewModel.State = OnBoardingPreferencesScreenViewModel.State(),
    uiEvent: (OnBoardingPreferencesScreenViewModel.Event) -> Unit = {},
    navigateBack: () -> Unit = {},
    navigateToDashboard: () -> Unit = {},
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            // region go back button
            Box(
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        start = 8.dp,
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { navigateBack() }
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
            // endregion

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Text(
                    text = stringResource(R.string.set_spendless_to_preferences),
                    style = LocalSpendlessTypography.current.headlineMedium,
                )

                Text(
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            bottom = 24.dp
                        ),
                    text = stringResource(R.string.how_to_change_preferences),
                    style = LocalSpendlessTypography.current.bodyMedium,
                )

                PreferencesComponentView(
                    expenseText = state.exampleText,
                    onExpenseFormatSelected = { uiEvent(OnBoardingPreferencesScreenViewModel.Event.OnExpensesFormatChanged(it)) },
                    selectedExpenseFormat = state.expensesFormat.getExpensesFormatsWithCurrency(state.selectedCurrency),
                    selectedCurrency = state.selectedCurrency,
                    onCurrencyChanged = { uiEvent(OnBoardingPreferencesScreenViewModel.Event.OnCurrencyChanged(it)) },
                    selectedDecimalSeparator = state.decimalSeparator.getDecimalSeparatorExample(),
                    onDecimalSeparatorSelected = { uiEvent(OnBoardingPreferencesScreenViewModel.Event.OnDecimalSeparatorChanged(it)) },
                    selectedThousandSeparator = state.thousandSeparator.getThousandSeparatorExample(),
                    onThousandSeparatorSelected = { uiEvent(OnBoardingPreferencesScreenViewModel.Event.OnThousandSeparatorChanged(it)) }
                )

                // region button
                SpendLessButton(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    enabled = state.isStartTrackingEnabled,
                    text = stringResource(R.string.start_tracking),
                    onClick = {
                        uiEvent(OnBoardingPreferencesScreenViewModel.Event.OnStartTrackingButtonClicked)
                        navigateToDashboard()
                    }
                )
                // endregion
            }
        }
    }
}

@Preview
@Composable
private fun PreferencesScreenPreview() {
    OnBoardingPreferencesScreen()
}