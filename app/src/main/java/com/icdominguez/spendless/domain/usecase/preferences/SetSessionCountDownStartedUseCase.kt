package com.icdominguez.spendless.domain.usecase.preferences

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import com.icdominguez.spendless.data.googletrusted.GoogleTrustedTimeManager
import javax.inject.Inject

class SetSessionCountDownStartedUseCase @Inject constructor(
    private val googleTrustedTimeManager: GoogleTrustedTimeManager,
    private val spendLessSharedPreferencesDataSource: SpendLessSharedPreferencesDataSource
) {
    operator fun invoke() {
        val sessionCountdownStartedTimestamp = googleTrustedTimeManager.getClient().computeCurrentUnixEpochMillis() ?: 0
        spendLessSharedPreferencesDataSource.setSessionCountDownStarted(sessionCountdownStartedTimestamp)
    }
}