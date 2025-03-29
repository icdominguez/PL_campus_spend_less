package com.icdominguez.spendless.presentation.screens.unauthorized.repeatpin

import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.preferences.GetPinFromSharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepeatPinScreenViewModel @Inject constructor(
    private val getPinFromSharedPreferencesUseCase: GetPinFromSharedPreferencesUseCase,
) : MviViewModel<RepeatPinScreenViewModel.State, RepeatPinScreenViewModel.Event>() {

    data class State(
        val numbers: List<Int> = emptyList(),
        val pinsMatch: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = "",
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
            is Event.OnNavigate -> {
                updateState {
                    copy(
                        numbers = emptyList(),
                        pinsMatch = false,
                        isError = false,
                        errorMessage = ""
                    )
                }
            }
            is Event.OnPinNumberClicked -> {
                updateState {
                    copy(
                        numbers = numbers.toMutableList().apply { add(event.number) }
                    )
                }
                if(state.value.numbers.size == 5) {
                    if(state.value.numbers.joinToString("").toInt() == getPinFromSharedPreferencesUseCase()) {
                        updateState { copy(pinsMatch = true) }
                    } else {
                        updateState {
                            copy(
                                isError = true,
                                errorMessage = "Pins don't match",
                            )
                        }
                    }
                }
            }
            is Event.OnRemoveButtonClicked -> {
                updateState {
                    copy(
                        numbers = numbers.toMutableList().apply { removeLastOrNull() }
                    )
                }
            }
            is Event.OnPinEntered -> {
                updateState {
                    copy(
                        isError = false,
                        errorMessage = "",
                        numbers = emptyList()
                    )
                }
            }
        }
    }
}