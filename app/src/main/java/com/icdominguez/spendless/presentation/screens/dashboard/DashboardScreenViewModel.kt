package com.icdominguez.spendless.presentation.screens.dashboard

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.core.formatTransaction
import com.icdominguez.spendless.domain.usecase.database.CheckRecurringTransactionsUseCase
import com.icdominguez.spendless.domain.usecase.database.GetTransactionsByUserUseCase
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.domain.usecase.ShouldShowAuthenticationScreenUseCase
import com.icdominguez.spendless.model.DecimalSeparator
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.model.ThousandSeparator
import com.icdominguez.spendless.presentation.model.Category
import com.icdominguez.spendless.presentation.model.Transaction
import com.icdominguez.spendless.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
    private val getUserLoggedInUseCase: GetUserLoggedInUseCase,
    private val getTransactionsByUserUseCase: GetTransactionsByUserUseCase,
    private val shouldShowAuthenticationScreenUseCase: ShouldShowAuthenticationScreenUseCase,
    private val checkRecurringTransactionsUseCase: CheckRecurringTransactionsUseCase,
) : MviViewModel<DashboardScreenViewModel.State, DashboardScreenViewModel.Event>() {

    private var transactionJob: Job? = null

    data class State(
        val isLoading: Boolean = false,
        val user: User? = null,
        val transactions: List<Transaction> = emptyList(),
        val accountBalance: String = "",
        val maxCategory: Category? = null,
        val lastWeekBalance: Double = 0.0,
        val shouldShowAuthenticationScreen: Boolean = false,
        val shouldShowModalBottomSheet: Boolean = false,
    )

    override var currentState = State()

    sealed class Event {
        data object OnNavigate: Event()
        data object OnAuthenticationFinished: Event()
        data object OnExportTransactionsButtonClicked: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnNavigate -> onNavigate()
            is Event.OnAuthenticationFinished -> onAuthenticationFinished()
            is Event.OnExportTransactionsButtonClicked -> checkIfAuthenticationScreenShouldBeShown()
        }
    }

    init {
        collectTransactions()
        viewModelScope.launch {
            val shouldShowAuthenticationScreen = shouldShowAuthenticationScreenUseCase()
            if(shouldShowAuthenticationScreen) {
                updateState { copy(shouldShowAuthenticationScreen = true) }
            }
        }
    }

    private fun collectTransactions() {
        transactionJob?.cancel()

        viewModelScope.launch {

            val user = getUserLoggedInUseCase()

            user?.let { userLoggedIn ->
                checkRecurringTransactionsUseCase(username = userLoggedIn.username)

                getTransactionsByUserUseCase(
                    username = userLoggedIn.username,
                    expenseFormat = ExpenseFormat.entries.first { it.name == userLoggedIn.expensesFormat },
                    decimalSeparator = userLoggedIn.decimalSeparator,
                    thousandSeparator = userLoggedIn.thousandSeparator,
                    currency = userLoggedIn.currency
                ).collect { transactionsBbdd ->

                    var accountBalance = 0.0
                    var lastWeekBalance = 0.0

                    transactionsBbdd.onEach { transaction ->
                        if (transaction.category != null) {
                            accountBalance -= transaction.amount
                        } else {
                            accountBalance += transaction.amount
                        }
                    }

                    val accountBalanceText = accountBalance.formatTransaction(
                        currency = userLoggedIn.currency,
                        expenseFormat = ExpenseFormat.entries.first { it.name == userLoggedIn.expensesFormat },
                        decimalSeparator = DecimalSeparator.entries.first { it.name == userLoggedIn.decimalSeparator }.symbol,
                        thousandSeparator = ThousandSeparator.entries.first { it.name == userLoggedIn.thousandSeparator }.symbol,
                        isExpense = accountBalance < 0,
                    )

                    val today = LocalDateTime.now()

                    val lastWeekStart =
                        today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    val lastWeekEnd = today.plusDays(6)

                    val lastWeekTransactions =
                        transactionsBbdd.filter { it.date in lastWeekStart..lastWeekEnd }
                    lastWeekTransactions.onEach { lastWeekTransaction ->
                        if (lastWeekTransaction.category != null) {
                            lastWeekBalance += lastWeekTransaction.amount
                        }
                    }

                    val maxCategoryInDatabase = transactionsBbdd
                        .filter { it.category != null }
                        .groupBy { it.category }
                        .mapValues { (_, transaction) -> transaction.sumOf { it.amount } }
                        .maxByOrNull { it.value }

                    val maxCategory =
                        Category.entries.firstOrNull { it.name == maxCategoryInDatabase?.key?.name }

                    updateState {
                        copy(
                            user = userLoggedIn,
                            transactions = transactionsBbdd,
                            accountBalance = accountBalanceText,
                            lastWeekBalance = lastWeekBalance,
                            isLoading = false,
                            maxCategory = maxCategory,
                        )
                    }
                }
            }
        }
    }

    private fun onAuthenticationFinished() {
        updateState {
            copy(shouldShowAuthenticationScreen = false)
        }
    }

    private fun checkIfAuthenticationScreenShouldBeShown() {
        viewModelScope.launch {
            val shouldShowAuthenticationScreen = shouldShowAuthenticationScreenUseCase()
            if(shouldShowAuthenticationScreen) {
                updateState { copy(shouldShowAuthenticationScreen = true) }
            }
        }
    }

    private fun onNavigate() {
        viewModelScope.launch {
            val shouldShowAuthenticationScreen = shouldShowAuthenticationScreenUseCase()
            if(shouldShowAuthenticationScreen) {
                updateState { copy(shouldShowAuthenticationScreen = true) }
            } else {
                collectTransactions()
            }
        }
    }
}