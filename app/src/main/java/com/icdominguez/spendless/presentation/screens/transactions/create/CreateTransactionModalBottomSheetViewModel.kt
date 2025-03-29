package com.icdominguez.spendless.presentation.screens.transactions.create

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.database.CreateTransactionUseCase
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.model.RecurringFrequency
import com.icdominguez.spendless.model.RecurringFrequencyItem
import com.icdominguez.spendless.model.getRecurringFrequencyItems
import com.icdominguez.spendless.presentation.model.CategoryItem
import com.icdominguez.spendless.presentation.model.TransactionType
import com.icdominguez.spendless.presentation.model.TransactionTypeItem
import com.icdominguez.spendless.presentation.model.User
import com.icdominguez.spendless.presentation.model.getCategoryItems
import com.icdominguez.spendless.presentation.model.getTransactionTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTransactionModalBottomSheetViewModel @Inject constructor(
    private val getUserLoggedInUseCase: GetUserLoggedInUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase
): MviViewModel<CreateTransactionModalBottomSheetViewModel.State, CreateTransactionModalBottomSheetViewModel.Event>() {
    override var currentState = State()

    data class State(
        val transceiverText: String = "",
        val amount: Double? = null,
        val note: String = "",
        val categories: List<CategoryItem> = getCategoryItems().mapIndexed { index, categoryItem -> categoryItem.copy(isSelected = index == 0) },
        val transactionTypes: List<TransactionTypeItem> = getTransactionTypes().mapIndexed { index, transactionTypeItem -> transactionTypeItem.copy(isSelected = index == 0) },
        val user: User? = null,
        val recurringFrequencyItems: List<RecurringFrequencyItem> = getRecurringFrequencyItems()
    ) {
        val isCreateTransactionButtonEnabled = transceiverText.isNotEmpty() && amount != null
    }

    sealed class Event {
        data class OnTransactionTypeChanged(val transactionType: TransactionTypeItem): Event()
        data class OnTransceiverTextChanged(val transceiverText: String): Event()
        data class OnAmountTextChanged(val amount: Double): Event()
        data class OnNoteTextChanged(val note: String): Event()
        data class OnCategoryChanged(val category: CategoryItem): Event()
        data object OnCreateTransactionButtonClicked: Event()
        data class OnRecurringFrequencyChanged(val recurringFrequency: RecurringFrequencyItem): Event()
        data object OnDialogOpened : Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnTransactionTypeChanged -> onTransactionTypeChanged(event.transactionType)
            is Event.OnTransceiverTextChanged -> onTransceiverTextChanged(event.transceiverText)
            is Event.OnAmountTextChanged -> onAmountTextChanged(event.amount)
            is Event.OnNoteTextChanged -> onNoteTextChanged(event.note)
            is Event.OnCategoryChanged -> onCategoryChanged(event.category)
            is Event.OnCreateTransactionButtonClicked -> onTransactionButtonClicked()
            is Event.OnRecurringFrequencyChanged -> onRecurringFrequencyChanged(event.recurringFrequency)
            is Event.OnDialogOpened -> {}
        }
    }

    init {
        viewModelScope.launch {
            val user = getUserLoggedInUseCase()
            updateState { copy(user = user) }
        }
    }

    private fun onTransactionTypeChanged(transactionType: TransactionTypeItem) {
        val transactions = state.value.transactionTypes.map {
            if(it == transactionType) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
        updateState { copy(transactionTypes = transactions) }
    }


    private fun onCategoryChanged(category: CategoryItem) {
        val categories = state.value.categories.map {
            if(it == category) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
        updateState { copy(categories = categories) }
    }

    private fun onNoteTextChanged(note: String) {
        updateState { copy(note = note) }
    }

    private fun onTransceiverTextChanged(transceiverText: String) {
        updateState { copy(transceiverText = transceiverText) }
    }

    private fun onAmountTextChanged(amount: Double) {
        updateState { copy(amount = amount) }
    }

    private fun onTransactionButtonClicked() {
        viewModelScope.launch {
            state.value.user?.let { userLoggedIn ->
                val selectedTransactionType = state.value.transactionTypes.first { it.isSelected }
                val selectedRecurringFrequency = state.value.recurringFrequencyItems.first { it.isSelected }

                createTransactionUseCase(
                    username = userLoggedIn.username,
                    transceiver = state.value.transceiverText,
                    category = if(selectedTransactionType.name == TransactionType.EXPENSE.name) state.value.categories.first { it.isSelected }.name else null,
                    amount = state.value.amount ?: 0.0,
                    note = state.value.note,
                    recurringFrequency = RecurringFrequency.valueOf(selectedRecurringFrequency.name)
                )

                updateState {
                    copy(
                        transceiverText = "",
                        categories = getCategoryItems().mapIndexed { index, categoryItem -> categoryItem.copy(isSelected = index == 0) },
                        amount = 0.0,
                        note = ""
                    )
                }
            }
        }
    }

    private fun onRecurringFrequencyChanged(recurringFrequency: RecurringFrequencyItem) {
        val recurringFrequencyItems = state.value.recurringFrequencyItems.map {
            if(it.name == recurringFrequency.name) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }

        updateState { copy(recurringFrequencyItems = recurringFrequencyItems) }
    }
}