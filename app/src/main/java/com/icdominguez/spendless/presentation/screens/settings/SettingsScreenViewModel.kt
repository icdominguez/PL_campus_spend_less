package com.icdominguez.spendless.presentation.screens.settings

import androidx.lifecycle.viewModelScope
import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.domain.usecase.database.UpdateUserLoggedInUseCase
import com.icdominguez.spendless.domain.usecase.ShouldShowAuthenticationScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val getUserLoggedInUseCAse: GetUserLoggedInUseCase,
    private val updateUserIsLoggedIn: UpdateUserLoggedInUseCase,
    private val shouldShowAuthenticationScreenUseCase: ShouldShowAuthenticationScreenUseCase,
) : MviViewModel<SettingsScreenViewModel.State, SettingsScreenViewModel.Event>() {
    data class State(
        val loading: Boolean = false,
        val shouldShowAuthenticationScreen: Boolean = false,
    )

    override var currentState = State()

    sealed class Event {
        data object OnLogOutButtonClicked : Event()
        data object OnPinEntered : Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.OnLogOutButtonClicked -> {
                onLogOutButtonClicked()
            }
            is Event.OnPinEntered -> {
                updateState {
                    copy(shouldShowAuthenticationScreen = false)
                }
            }
        }
    }

    private fun onLogOutButtonClicked() {
        viewModelScope.launch {
            val user = getUserLoggedInUseCAse()
            user?.let {
                updateUserIsLoggedIn(
                    username = it.username,
                    isLoggedIn = false
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            val shouldShowAuthenticationScreen = shouldShowAuthenticationScreenUseCase()
            if(shouldShowAuthenticationScreen) {
                updateState {
                    copy(shouldShowAuthenticationScreen = true)
                }
            }
        }
    }
}