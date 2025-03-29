package com.icdominguez.spendless

import com.icdominguez.spendless.core.MviViewModel
import com.icdominguez.spendless.data.googletrusted.GoogleTrustedTimeManager
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getUserLoggedInUseCase: GetUserLoggedInUseCase,
    private val googleTrustedTimeManager: GoogleTrustedTimeManager
) : MviViewModel<MainActivityViewModel.State, MainActivityViewModel.Event>() {

    override var currentState: State = State()

    data class State(
        val loading: Boolean = false
    )

    sealed class Event {
        data object IsLoading: Event()
    }

    override fun uiEvent(event: Event) {
        when(event) {
            is Event.IsLoading -> {

            }
        }
    }
}