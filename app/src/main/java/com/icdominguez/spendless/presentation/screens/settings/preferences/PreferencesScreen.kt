package com.icdominguez.spendless.presentation.screens.settings.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.icdominguez.spendless.R
import com.icdominguez.spendless.presentation.designsystem.composables.CustomLoadingIndicator
import com.icdominguez.spendless.presentation.designsystem.composables.PreferencesComponentView
import com.icdominguez.spendless.presentation.designsystem.composables.ScreenHeader
import com.icdominguez.spendless.presentation.designsystem.composables.SpendLessButton
import com.icdominguez.spendless.presentation.designsystem.theme.LocalSpendlessColorsPalette

@Composable
fun PreferencesScreen(
    state: PreferencesScreenViewModel.State = PreferencesScreenViewModel.State(),
    uiEvent: (PreferencesScreenViewModel.Event) -> Unit = {},
    navigateToAuthentication: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            ScreenHeader(
                label = stringResource(R.string.settings_screen_preferences),
                onNavigateBackClicked = { navigateBack() }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                if(state.isLoading) {
                    CustomLoadingIndicator(color = LocalSpendlessColorsPalette.current.primary,)
                } else {
                    PreferencesComponentView(
                        expenseText = state.expensesText,
                        onExpenseFormatSelected = { uiEvent(PreferencesScreenViewModel.Event.OnExpensesFormatChanged(it)) },
                        selectedExpenseFormat = state.expensesFormat.getExpensesFormatsWithCurrency(state.selectedCurrency),
                        selectedCurrency = state.selectedCurrency,
                        onCurrencyChanged = { uiEvent(PreferencesScreenViewModel.Event.OnCurrencyChanged(it)) },
                        selectedDecimalSeparator = state.decimalSeparator.getDecimalSeparatorExample(),
                        onDecimalSeparatorSelected = { uiEvent(PreferencesScreenViewModel.Event.OnDecimalSeparatorChanged(it)) },
                        selectedThousandSeparator = state.thousandSeparator.getThousandSeparatorExample(),
                        onThousandSeparatorSelected = { uiEvent(PreferencesScreenViewModel.Event.OnThousandSeparatorChanged(it)) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SpendLessButton(
                        enabled = state.isSaveButtonEnabled,
                        text = stringResource(R.string.save),
                        onClick = {
                            uiEvent(PreferencesScreenViewModel.Event.OnSaveButtonClicked)
                        }
                    )
                }
            }
        }
    }

    if(state.shouldShowAuthenticationScreen) {
        navigateToAuthentication()
        uiEvent(PreferencesScreenViewModel.Event.OnNavigatedToAuthentication)
    }

    if(state.shouldNavigateBack) {
        navigateBack()
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview(modifier: Modifier = Modifier) {
    PreferencesScreen()
}