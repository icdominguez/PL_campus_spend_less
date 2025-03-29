package com.icdominguez.spendless.domain.usecase.preferences

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import com.icdominguez.spendless.data.googletrusted.GoogleTrustedTimeManager
import javax.inject.Inject

class SetTimeAppClosedUseCase @Inject constructor(
    private val googleTrustedTimeManager: GoogleTrustedTimeManager,
    private val spendLessSharedPreferencesDataSource: SpendLessSharedPreferencesDataSource,
) {
    operator fun invoke() {
        val time = googleTrustedTimeManager.getClient().computeCurrentUnixEpochMillis()
        time?.let {
            spendLessSharedPreferencesDataSource.setTimeAppClosed(it)
        }
    }
}