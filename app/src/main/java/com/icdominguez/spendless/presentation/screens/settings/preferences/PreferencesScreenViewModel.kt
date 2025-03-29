package com.icdominguez.spendless.presentation.screens.settings.preferences

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.domain.usecase.database.UpdateUserPreferencesUseCase
import com.icdominguez.spendless.domain.usecase.ShouldShowAuthenticationScreenUseCase
import com.icdominguez.spendless.model.Currency
import com.icdominguez.spendless.model.DecimalSeparator
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.model.ThousandSeparator
import com.icdominguez.spendless.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesScreenViewModel @Inject constructor(
    private val getUserLoggedInUseCase: GetUserLoggedInUseCase,
    private val updateUserPreferencesUseCase: UpdateUserPreferencesUseCase,
    private val shouldShowAuthenticationScreenUseCase: ShouldShowAuthenticationScreenUseCase,
): MviViewModel<PreferencesScreenViewModel.State, PreferencesScreenViewModel.Event>() {

    data class State(
        val user: User? = null,
        val expensesText: String = "10.283,45",
        val expensesFormat: ExpenseFormat = ExpenseFormat.LESS,
        val thousandSeparator: ThousandSeparator = ThousandSeparator.DOT,
        val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
        val selectedCurrency: Currency = Currency.USD,
        val isLoading: Boolean = false,
        val shouldShowAuthenticationScreen: Boolean = false,
        val shouldNavigateBack: Boolean = false,
    ) {
        val isSaveButtonEnabled: Boolean = decimalSeparator.symbol != thousandSeparator.symbol
    }

    override var currentState: State = State()

    sealed class Event {
        data class OnExpensesFormatChanged(val expensesFormat: String): Event()
        data class OnDecimalSeparatorChanged(val decimalSeparator: String): Event()
        data class OnThousandSeparatorChanged(val thousandSeparator: String): Event()
        data class OnCurrencyChanged(val currency: Currency): Event()
        data object OnSaveButtonClicked: Event()
        data object OnPinEntered: Event()
        data object OnNavigatedToAuthentication: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnExpensesFormatChanged -> onExpensesFormatChanged(event.expensesFormat)
            is Event.OnDecimalSeparatorChanged -> onDecimalSeparatorChanged(event.decimalSeparator)
            is Event.OnThousandSeparatorChanged -> onThousandSeparatorChanged(event.thousandSeparator)
            is Event.OnCurrencyChanged -> onCurrencyChanged(event.currency)
            is Event.OnSaveButtonClicked -> checkIfAuthenticationNeedsToBeShown()
            is Event.OnPinEntered -> onSaveButtonClicked()
            is Event.OnNavigatedToAuthentication -> onNavigatedToAuthentication()
        }
    }

    init {
        viewModelScope.launch {
            val user = getUserLoggedInUseCase()

            user?.let {
                updateState {
                    copy(
                        user = user,
                        expensesFormat = ExpenseFormat.entries.first { it.name == user.expensesFormat },
                        thousandSeparator = ThousandSeparator.entries.first { it.name == user.thousandSeparator },
                        decimalSeparator = DecimalSeparator.entries.first { it.name == user.decimalSeparator },
                        selectedCurrency = Currency.entries.first { it.symbol == user.currency },
                    )
                }
                updateExampleText()
            }
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
                expensesText = when(expensesFormat) {
                    ExpenseFormat.LESS -> { "-${selectedCurrency.symbol}10${thousandSeparator.symbol}283${decimalSeparator.symbol}45" }
                    ExpenseFormat.PARENTHESIS -> { "(${selectedCurrency.symbol}10${thousandSeparator.symbol}283${decimalSeparator.symbol}45)" }
                }
            )
        }
    }

    private fun onSaveButtonClicked() {
        viewModelScope.launch {
            state.value.user?.let { userLoggedIn ->
                updateUserPreferencesUseCase(
                    username = userLoggedIn.username,
                    expenseFormat = state.value.expensesFormat.name,
                    currencySymbol = state.value.selectedCurrency.symbol,
                    decimalSeparator = state.value.decimalSeparator.name,
                    thousandSeparator = state.value.thousandSeparator.name
                )

                updateState {
                    copy(shouldNavigateBack = true)
                }
            }
        }
    }

    private fun checkIfAuthenticationNeedsToBeShown() {
        viewModelScope.launch {
            val shouldShowAuthenticationScreen = shouldShowAuthenticationScreenUseCase
            if(shouldShowAuthenticationScreen()) {
                updateState { copy(shouldShowAuthenticationScreen = true) }
            } else {
                onSaveButtonClicked()
            }
        }
    }

    private fun onNavigatedToAuthentication() {
        updateState {
            copy(shouldShowAuthenticationScreen = false)
        }
    }
}
