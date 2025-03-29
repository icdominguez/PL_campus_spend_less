package com.icdominguez.spendless.domain.usecase

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import com.icdominguez.spendless.data.googletrusted.GoogleTrustedTimeManager
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.presentation.model.SessionExpiryDuration
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class ShouldShowAuthenticationScreenUseCase @Inject constructor(
    private val getUserLoggedInUseCase: GetUserLoggedInUseCase,
    private val googleTrustedTimeManager: GoogleTrustedTimeManager,
    private val spendLessSharedPreferencesDataSource: SpendLessSharedPreferencesDataSource
) {
    suspend operator fun invoke(): Boolean {
        var shouldShowAuthenticationScreen = false
        val user = getUserLoggedInUseCase()

        user?.let { loggedInUser ->
            val sessionExpiryDuration = SessionExpiryDuration.entries.first { it.name == loggedInUser.sessionExpiryDuration }
            val currentTime = googleTrustedTimeManager.getClient().computeCurrentUnixEpochMillis()
            val sessionCountdownStartedTimestamp = spendLessSharedPreferencesDataSource.getSessionCountDownStarted() + sessionExpiryDuration.millis

            currentTime?.let { validCurrentTime ->
                val instantTimeAppClosed = Instant.ofEpochMilli(sessionCountdownStartedTimestamp)
                val instantCurrentTime = Instant.ofEpochMilli(validCurrentTime)
                if(Duration.between(instantCurrentTime, instantTimeAppClosed).toMillis() < 0) {
                    shouldShowAuthenticationScreen = true
                }
            }
        }

        return shouldShowAuthenticationScreen
    }
}