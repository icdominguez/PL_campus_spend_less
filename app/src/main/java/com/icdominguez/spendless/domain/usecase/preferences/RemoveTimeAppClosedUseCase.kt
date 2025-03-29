package com.icdominguez.spendless.domain.usecase.preferences

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import javax.inject.Inject

class RemoveTimeAppClosedUseCase @Inject constructor(
    private val spendLessSharedPreferencesDataSource: SpendLessSharedPreferencesDataSource,
) {
    operator fun invoke() =
        spendLessSharedPreferencesDataSource.removeTimeAppClosed()
}