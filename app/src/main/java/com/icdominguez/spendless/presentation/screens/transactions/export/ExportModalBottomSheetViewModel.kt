package com.icdominguez.spendless.presentation.screens.transactions.export

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.ExportTransactionsUseCase
import com.icdominguez.spendless.domain.usecase.ShouldShowAuthenticationScreenUseCase
import com.icdominguez.spendless.presentation.model.ExportOption
import com.icdominguez.spendless.presentation.model.ExportOptionItem
import com.icdominguez.spendless.presentation.model.getExportOptionItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportModalBottomSheetViewModel @Inject constructor(
    private val exportTransactionsUseCase: ExportTransactionsUseCase,
    private val shouldShowAuthenticationScreen: ShouldShowAuthenticationScreenUseCase,
): MviViewModel<ExportModalBottomSheetViewModel.State, ExportModalBottomSheetViewModel.Event>() {
    data class State(
        val exportOptions: List<ExportOptionItem> = getExportOptionItems(),
        val shouldShowAuthenticationScreen: Boolean = false,
    )

    override var currentState: State = State()

    sealed class Event {
        data class OnExportItemClicked(val exportOptionSelected: ExportOptionItem): Event()
        data object OnExportButtonClicked: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnExportItemClicked -> onExportItemClicked(event.exportOptionSelected)
            is Event.OnExportButtonClicked -> onExportButtonClicked()
        }
    }

    private fun onExportItemClicked(exportOptionSelected: ExportOptionItem) {
        val exportOptionsItems = state.value.exportOptions.map {
            if(it.name == exportOptionSelected.name) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }

        updateState { copy(exportOptions = exportOptionsItems) }
    }

    private fun onExportButtonClicked() {
        val selectedExportOptionItem = state.value.exportOptions.first { it.isSelected }
        val exportOption = ExportOption.entries.first { it.name == selectedExportOptionItem.name }
        viewModelScope.launch {
            exportTransactionsUseCase(exportOption)
        }
    }
}