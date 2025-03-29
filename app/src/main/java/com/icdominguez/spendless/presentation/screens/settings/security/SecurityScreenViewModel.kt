package com.icdominguez.spendless.presentation.screens.settings.security

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.domain.usecase.database.UpdateSessionExpiryAndLockedOutDurationUseCase
import com.icdominguez.spendless.domain.usecase.ShouldShowAuthenticationScreenUseCase
import com.icdominguez.spendless.presentation.model.LockedOutDuration
import com.icdominguez.spendless.presentation.model.SessionExpiryDuration
import com.icdominguez.spendless.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityScreenViewModel @Inject constructor(
    private val getUserLoggedInUseCAse: GetUserLoggedInUseCase,
    private val updateSessionExpiryAndLockedOutDurationUseCase: UpdateSessionExpiryAndLockedOutDurationUseCase,
    private val shouldShowAuthenticationScreenUseCase: ShouldShowAuthenticationScreenUseCase,
) : MviViewModel<SecurityScreenViewModel.State, SecurityScreenViewModel.Event>() {
    override var currentState = State()

    data class State(
        val user: User? = null,
        val sessionExpiryDuration: SessionExpiryDuration = SessionExpiryDuration.MINUTES_5,
        val lockedOutDuration: LockedOutDuration = LockedOutDuration.SECONDS_15,
        val shouldShowAuthenticationScreen: Boolean = false,
    )

    sealed class Event {
        data class OnSessionExpiryDurationSelected(val sessionExpiryDuration: String) : Event()
        data class OnLockedOutDurationSelected(val lockedOutDuration: String) : Event()
        data object OnSaveButtonClicked: Event()
        data object OnPinEntered: Event()
    }

    override fun uiEvent(event: Event) {
        when (event) {
            is Event.OnSessionExpiryDurationSelected -> onSessionExpiryDurationSelected(event.sessionExpiryDuration)
            is Event.OnLockedOutDurationSelected -> onLockedOutDuration(event.lockedOutDuration)
            is Event.OnSaveButtonClicked -> checkIfAuthenticationNeedsToBeShown()
            is Event.OnPinEntered -> onSaveButtonClicked()
        }
    }

    init {
        viewModelScope.launch {
            val user = getUserLoggedInUseCAse()
            user?.let { userLoggedIn ->
                updateState {
                    copy(
                        user = userLoggedIn,
                        sessionExpiryDuration = SessionExpiryDuration.entries.first { it.name == userLoggedIn.sessionExpiryDuration },
                        lockedOutDuration = LockedOutDuration.entries.first { it.name == userLoggedIn.lockedOutDuration }
                    )
                }
            }
        }
    }

    private fun onSessionExpiryDurationSelected(sessionExpiryDuration: String) {
        updateState { copy(sessionExpiryDuration = SessionExpiryDuration.entries.first { it.value == sessionExpiryDuration }) }
    }

    private fun onLockedOutDuration(lockedOutDuration: String) {
        updateState { copy(lockedOutDuration = LockedOutDuration.entries.first { it.value == lockedOutDuration }) }
    }

    private fun onSaveButtonClicked() {
        viewModelScope.launch {
            state.value.user?.let {
                updateSessionExpiryAndLockedOutDurationUseCase(
                    username = it.username,
                    sessionExpiryDuration = currentState.sessionExpiryDuration.name,
                    lockedOutDuration = currentState.lockedOutDuration.name,
                )
            }
        }
    }

    private fun checkIfAuthenticationNeedsToBeShown() {
        viewModelScope.launch {
            val shouldShowAuthenticationScreen = shouldShowAuthenticationScreenUseCase
            if(shouldShowAuthenticationScreen()) {
                updateState { copy(shouldShowAuthenticationScreen = true) }
            }
        }
    }
}