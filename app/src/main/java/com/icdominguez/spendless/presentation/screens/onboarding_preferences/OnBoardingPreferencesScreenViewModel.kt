package com.icdominguez.spendless.presentation.screens.onboarding_preferences

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.database.InsertNewUserUseCase
import com.icdominguez.spendless.domain.usecase.preferences.GetPinFromSharedPreferencesUseCase
import com.icdominguez.spendless.domain.usecase.preferences.GetUsernameFromSharedPreferencesUseCase
import com.icdominguez.spendless.model.Currency
import com.icdominguez.spendless.model.DecimalSeparator
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.model.ThousandSeparator
import com.icdominguez.spendless.presentation.model.LockedOutDuration
import com.icdominguez.spendless.presentation.model.SessionExpiryDuration
import com.icdominguez.spendless.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingPreferencesScreenViewModel @Inject constructor(
    private val insertNewUserUseCase: InsertNewUserUseCase,
    private val getUsernameFromSharedPreferencesUseCase: GetUsernameFromSharedPreferencesUseCase,
    private val getPinFromSharedPreferencesUseCase: GetPinFromSharedPreferencesUseCase,
): MviViewModel<OnBoardingPreferencesScreenViewModel.State, OnBoardingPreferencesScreenViewModel.Event>() {

    data class State(
        val exampleText: String = "",
        val expensesFormat: ExpenseFormat = ExpenseFormat.LESS,
        val thousandSeparator: ThousandSeparator = ThousandSeparator.COMMA,
        val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
        val selectedCurrency: Currency = Currency.USD
    ) {
        val isStartTrackingEnabled: Boolean = decimalSeparator.symbol != thousandSeparator.symbol
    }

    override var currentState = State()

    init {
        updateExampleText()
    }

    sealed class Event {
        data class OnExpensesFormatChanged(val expensesFormat: String): Event()
        data class OnDecimalSeparatorChanged(val decimalSeparator: String): Event()
        data class OnThousandSeparatorChanged(val thousandSeparator: String): Event()
        data class OnCurrencyChanged(val currency: Currency): Event()
        data object OnStartTrackingButtonClicked: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnExpensesFormatChanged -> onExpensesFormatChanged(event.expensesFormat)
            is Event.OnDecimalSeparatorChanged -> onDecimalSeparatorChanged(event.decimalSeparator)
            is Event.OnThousandSeparatorChanged -> onThousandSeparatorChanged(event.thousandSeparator)
            is Event.OnCurrencyChanged -> onCurrencyChanged(event.currency)
            is Event.OnStartTrackingButtonClicked -> onStartTrackingButtonClicked()
        }
    }

    private fun onExpensesFormatChanged(expenseFormat: String) {
        updateState { copy(expensesFormat = ExpenseFormat.entries.first { it.getExpensesFormatsWithCurrency(state.value.selectedCurrency) == expenseFormat }) }
        updateExampleText()
    }

    private fun onDecimalSeparatorChanged(decimalSeparator: String) {
        updateState { copy(decimalSeparator = DecimalSeparator.entries.first { it.getDecimalSeparatorExample() == decimalSeparator }) }
        updateExampleText()
    }

    private fun onThousandSeparatorChanged(thousandSeparator: String) {
        updateState { copy(thousandSeparator = ThousandSeparator.entries.first { it.getThousandSeparatorExample() == thousandSeparator }) }
        updateExampleText()
    }

    private fun onCurrencyChanged(currency: Currency) {
        updateState { copy(selectedCurrency = currency) }
        updateExampleText()
    }

    private fun updateExampleText() {
        updateState {
            copy(
                exampleText = when(expensesFormat) {
                    ExpenseFormat.LESS -> { "-${selectedCurrency.symbol}10${thousandSeparator.symbol}283${decimalSeparator.symbol}45" }
                    ExpenseFormat.PARENTHESIS -> { "(${selectedCurrency.symbol}10${thousandSeparator.symbol}283${decimalSeparator.symbol}45)" }
                }
            )
        }
    }

    private fun onStartTrackingButtonClicked() {
        viewModelScope.launch {
            val username = getUsernameFromSharedPreferencesUseCase()
            val pin = getPinFromSharedPreferencesUseCase()

            if(username != null && pin != 0) {
                insertNewUserUseCase(
                    user = User(
                        username = username,
                        pin = pin,
                        currency = currentState.selectedCurrency.symbol,
                        expensesFormat = currentState.expensesFormat.name,
                        thousandSeparator = currentState.thousandSeparator.name,
                        decimalSeparator = currentState.decimalSeparator.name,
                        sessionExpiryDuration = SessionExpiryDuration.MINUTES_5.name,
                        lockedOutDuration = LockedOutDuration.SECONDS_15.name,
                        isLoggedIn = true
                    )
                )
            }
        }
    }
}