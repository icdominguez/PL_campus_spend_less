package com.icdominguez.spendless.presentation.screens.authentication

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.Commons.PIN_ATTEMPTS
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.domain.usecase.database.UpdateUserLoggedInUseCase
import com.icdominguez.spendless.domain.usecase.preferences.GetLockedOutDurationCountdownDifferenceUseCase
import com.icdominguez.spendless.domain.usecase.preferences.RemoveTimeAppClosedUseCase
import com.icdominguez.spendless.domain.usecase.preferences.SetSessionCountDownStartedUseCase
import com.icdominguez.spendless.domain.usecase.preferences.SetTimeAppClosedUseCase
import com.icdominguez.spendless.presentation.model.LockedOutDuration
import com.icdominguez.spendless.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationScreenViewModel @Inject constructor(
    private val getUserLoggedInUseCase: GetUserLoggedInUseCase,
    private val updateUserLoggedInUseCase: UpdateUserLoggedInUseCase,
    private val setTimeAppClosedUseCase: SetTimeAppClosedUseCase,
    private val getLockedOutDurationCountdownDifferenceUseCase: GetLockedOutDurationCountdownDifferenceUseCase,
    private val removeTimeAppClosedUseCase: RemoveTimeAppClosedUseCase,
    private val setSessionCountDownStartedUseCase: SetSessionCountDownStartedUseCase
) : MviViewModel<AuthenticationScreenViewModel.State, AuthenticationScreenViewModel.Event>() {

    data class State(
        val user: User? = null,
        val numbers: List<Int> = emptyList(),
        val attemptsRemaining: Int = PIN_ATTEMPTS,
        val hasMoreAttempts: Boolean = true,
        val shouldNavigate: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = "",
        val remainingSeconds: Long? = null,
    )

    override var currentState = State()

    sealed class Event {
        data object OnLogOutButtonClicked : Event()
        data class OnPinNumberClicked(val number: Int) : Event()
        data object OnRemoveButtonClicked : Event()
        data object OnPinEntered: Event()
        data object OnErrorShown: Event()
        data object OnCountDownFinished: Event()
        data object OnNavigated: Event()
    }

    init {
        viewModelScope.launch {
            val user = getUserLoggedInUseCase()
            user?.let { currentUser ->
                val lockedOutDuration = LockedOutDuration.entries.first { it.name == currentUser.lockedOutDuration }
                val lockedOutDurationCountdownRemainingSeconds = getLockedOutDurationCountdownDifferenceUseCase(
                    userLockedOutDuration = lockedOutDuration
                )

                if(lockedOutDurationCountdownRemainingSeconds > 0L) {
                    updateState {
                        copy(
                            user = currentUser,
                            hasMoreAttempts = false,
                            attemptsRemaining = 0,
                            remainingSeconds = lockedOutDurationCountdownRemainingSeconds
                        )
                    }
                } else {
                    updateState {
                        copy(user = user)
                    }

                }
            }
        }
    }

    override fun uiEvent(event: Event) {
        when (event) {
            is Event.OnLogOutButtonClicked -> onLogOutButtonClicked()
            is Event.OnPinNumberClicked -> onPinNumberClicked(event.number)
            is Event.OnRemoveButtonClicked -> onRemoveButtonClicked()
            is Event.OnPinEntered -> {
                if (state.value.numbers.joinToString("").toInt() == state.value.user?.pin!!) {
                    removeTimeAppClosedUseCase()
                    setSessionCountDownStartedUseCase()
                    updateState { copy(shouldNavigate = true) }
                } else {
                    val attempts = state.value.attemptsRemaining - 1

                    updateState {
                        copy(
                            hasMoreAttempts = attempts != 0,
                            attemptsRemaining = attempts,
                            isError = true,
                            errorMessage = "Pins don't match",
                            numbers = emptyList()
                        )
                    }

                    if(attempts == 0) {
                        setTimeAppClosedUseCase()
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
            is Event.OnCountDownFinished -> {
                updateState {
                    copy(
                        hasMoreAttempts = true,
                        attemptsRemaining = PIN_ATTEMPTS,
                        remainingSeconds = null
                    )
                }
            }
            is Event.OnNavigated -> {
                updateState {
                    copy(
                        user = null,
                        shouldNavigate = false,
                        numbers = emptyList(),
                        attemptsRemaining = PIN_ATTEMPTS,
                        hasMoreAttempts = true,
                        isError = false,
                        errorMessage = "",
                        remainingSeconds = null
                    )
                }
            }
        }
    }

    private fun onLogOutButtonClicked() {
        viewModelScope.launch {
            state.value.user?.let {
                updateUserLoggedInUseCase(
                    username = it.username,
                    isLoggedIn = false
                )
            }
        }
    }

    private fun onPinNumberClicked(number: Int) {
        updateState {
            copy(numbers = numbers.toMutableList().apply { add(number) })
        }
    }

    private fun onRemoveButtonClicked() {
        updateState {
            copy(
                numbers = numbers.toMutableList().apply { removeLastOrNull() }
            )
        }
    }
}