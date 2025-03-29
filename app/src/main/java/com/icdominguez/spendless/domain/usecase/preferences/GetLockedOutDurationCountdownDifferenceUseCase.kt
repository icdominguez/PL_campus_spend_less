package com.icdominguez.spendless.domain.usecase.preferences

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import com.icdominguez.spendless.data.googletrusted.GoogleTrustedTimeManager
import com.icdominguez.spendless.presentation.model.LockedOutDuration
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class GetLockedOutDurationCountdownDifferenceUseCase @Inject constructor(
    private val googleTrustedTimeManager: GoogleTrustedTimeManager,
    private val spendLessSharedPreferencesDataSource: SpendLessSharedPreferencesDataSource,
) {
    operator fun invoke(userLockedOutDuration: LockedOutDuration): Long {
        val timeAppClosed = spendLessSharedPreferencesDataSource.getTimeAppClosed() + userLockedOutDuration.millis
        val currentTime = googleTrustedTimeManager.getClient().computeCurrentUnixEpochMillis()

        return currentTime?.let {
            val instantTimeAppClosed = Instant.ofEpochMilli(timeAppClosed)
            val instantCurrentTime = Instant.ofEpochMilli(currentTime)
            Duration.between(instantCurrentTime, instantTimeAppClosed).toMillis()
        } ?: 0L
    }
}