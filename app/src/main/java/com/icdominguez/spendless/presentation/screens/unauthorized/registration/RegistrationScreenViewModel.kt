package com.icdominguez.spendless.presentation.screens.unauthorized.registration

import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.preferences.SetUsernameInSharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val setUsernameInSharedPreferencesUseCase: SetUsernameInSharedPreferencesUseCase,
): MviViewModel<RegistrationScreenViewModel.State, RegistrationScreenViewModel.Event>() {

    companion object {
        private const val MIN_USERNAME_LENGTH = 3
        private const val MAX_USERNAME_LENGTH = 14
    }

    data class State(
        val usernameText: String = "",
        val isError: Boolean = false,
        val errorMessage: String = "",
        val shouldNavigate: Boolean = false,
    ) {
        val isNextButtonEnabled = usernameText.isNotEmpty()
    }

    override var currentState = State()

    sealed class Event {
        data class OnUsernameTextChanged(val username: String) : Event()
        data object OnNextButtonClicked : Event()
        data object OnErrorShown: Event()
        data object OnNavigate: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnUsernameTextChanged -> {
                updateState { copy(usernameText = event.username) }
            }
            is Event.OnNextButtonClicked -> {
                if(state.value.usernameText.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH) {
                    setUsernameInSharedPreferencesUseCase(currentState.usernameText)
                    updateState { copy(shouldNavigate = true) }
                } else {
                    updateState {
                        copy(
                            isError = true,
                            errorMessage = "Username length must be between $MIN_USERNAME_LENGTH and $MAX_USERNAME_LENGTH characters"
                        )
                    }
                }
            }
            is Event.OnErrorShown -> {
                updateState {
                    copy(
                        isError = false,
                        errorMessage = ""
                    )
                }
            }
            is Event.OnNavigate -> {
                updateState { copy(shouldNavigate = false) }
            }
        }
    }
}