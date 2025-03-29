package com.icdominguez.spendless.presentation.screens.unauthorized.login

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.database.GetUserUseCase
import com.icdominguez.spendless.domain.usecase.database.UpdateUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserLoggedInUseCase: UpdateUserLoggedInUseCase
) : MviViewModel<LoginScreenViewModel.State, LoginScreenViewModel.Event>() {
    data class State(
        val usernameText: String = "",
        val pinText: String = "",
        val isError: Boolean = false,
        val errorMessage: String = "",
        val shouldNavigate: Boolean = false,
    ) {
        val isLoginButtonEnabled: Boolean = usernameText.isNotBlank() && pinText.isNotBlank()
    }

    override var currentState = State()

    sealed class Event {
        data class OnUsernameTextChange(val username: String) : Event()
        data class OnPinTextChange(val pin: String) : Event()
        data object OnLoginButtonClicked : Event()
        data object OnBannerShown: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnUsernameTextChange -> {
                updateState { copy(usernameText = event.username) }
            }
            is Event.OnPinTextChange -> {
                updateState { copy(pinText = event.pin) }
            }
            is Event.OnLoginButtonClicked -> {
                viewModelScope.launch {
                    val user = getUserUseCase(currentState.usernameText, currentState.pinText.toInt())
                    if(user != null) {
                        updateUserLoggedInUseCase(
                            username = user.username,
                            isLoggedIn = true
                        )
                        updateState { copy(shouldNavigate = true) }
                    } else {
                        updateState {
                            copy(
                                isError = true,
                                errorMessage = "Incorrect info. Check username and PIN"
                            )
                        }
                    }
                }
            }
            is Event.OnBannerShown -> {
                updateState {
                    copy(
                        isError = false,
                        errorMessage = "",
                    )
                }
            }
        }
    }
}