package com.icdominguez.spendless.domain.usecase.preferences

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import javax.inject.Inject

class GetUsernameFromSharedPreferencesUseCase @Inject constructor(
    private val spendLessSharedPreferencesDataSource: SpendLessSharedPreferencesDataSource
) {
    operator fun invoke(): String? =
        spendLessSharedPreferencesDataSource.getUsername()
}
