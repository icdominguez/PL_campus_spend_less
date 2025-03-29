package com.icdominguez.spendless.presentation.screens.transactions

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.database.GetTransactionsByUserUseCase
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.domain.usecase.ShouldShowAuthenticationScreenUseCase
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.presentation.model.Transaction
import com.icdominguez.spendless.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTransactionScreenViewModel @Inject constructor(
    private val getUserLoggedInUseCase: GetUserLoggedInUseCase,
    private val getTransactionsByUserUseCase: GetTransactionsByUserUseCase,
    private val shouldShowAuthenticationScreenUseCase: ShouldShowAuthenticationScreenUseCase,
) : MviViewModel<AllTransactionScreenViewModel.State, AllTransactionScreenViewModel.Event>() {

    data class State(
        val user: User? = null,
        val isLoading: Boolean = false,
        val transactions: List<Transaction> = emptyList(),
        val shouldShowAuthenticationScreen: Boolean = false,
    )

    override var currentState = State()

    sealed class Event {
        data object OnPinEntered: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnPinEntered -> {
                updateState {
                    copy(
                        shouldShowAuthenticationScreen = false
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val user = getUserLoggedInUseCase()

            user?.let {
                getTransactionsByUserUseCase(
                    username = user.username,
                    currency = user.currency,
                    expenseFormat = ExpenseFormat.entries.first { it.name == user.expensesFormat },
                    decimalSeparator = user.decimalSeparator,
                    thousandSeparator = user.thousandSeparator,
                ).collect { transactions ->
                    updateState {
                        copy(
                            user = user,
                            isLoading = false,
                            transactions = transactions
                        )
                    }
                }
            }
        }

        checkIfAuthenticationScreenNeedsToBeShown()
    }

    private fun checkIfAuthenticationScreenNeedsToBeShown() {
        viewModelScope.launch {
            val showShowAuthenticationScreen = shouldShowAuthenticationScreenUseCase()
            updateState {
                copy(
                    shouldShowAuthenticationScreen = showShowAuthenticationScreen
                )
            }
        }
    }
}