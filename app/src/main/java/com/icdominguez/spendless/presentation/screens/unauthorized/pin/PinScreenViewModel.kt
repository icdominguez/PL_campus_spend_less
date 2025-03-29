package com.icdominguez.spendless.presentation.screens.unauthorized.pin

import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.preferences.SetPinInSharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinScreenViewModel @Inject constructor(
    private val setPinInSharedPreferencesUseCase: SetPinInSharedPreferencesUseCase
): MviViewModel<PinScreenViewModel.State, PinScreenViewModel.Event>() {

    data class State(
        val numbers: List<Int> = emptyList(),
    )

    override var currentState = State()

    sealed class Event {
        data class OnPinNumberClicked(val number: Int): Event()
        data object OnRemoveButtonClicked: Event()
        data object OnPinEntered: Event()
        data object OnNavigate: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnNavigate -> onNavigate()
            is Event.OnPinNumberClicked -> onPinNumberClicked(event.number)
            is Event.OnRemoveButtonClicked -> onRemoveButtonClicked()
            is Event.OnPinEntered -> onPinEntered()
        }
    }

    private fun onNavigate() {
        updateState {
            copy(numbers = emptyList())
        }
    }

    private fun onPinNumberClicked(number: Int) {
        updateState {
            copy(
                numbers = numbers.toMutableList().apply { add(number) },
            )
        }
        if(state.value.numbers.size == 5) {
            setPinInSharedPreferencesUseCase(pin = currentState.numbers.joinToString("").toInt())
        }
    }

    private fun onRemoveButtonClicked() {
        updateState {
            copy(
                numbers = numbers.toMutableList().apply { removeLastOrNull() }
            )
        }
    }

    private fun onPinEntered() {
        setPinInSharedPreferencesUseCase(pin = currentState.numbers.joinToString("").toInt())
    }
}