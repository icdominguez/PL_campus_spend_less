package com.icdominguez.spendless.domain.usecase.preferences

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import javax.inject.Inject

class SetPinInSharedPreferencesUseCase @Inject constructor(
    private val spendLessSharedPreferencesDataSource: SpendLessSharedPreferencesDataSource
) {
    operator fun invoke(pin: Int) =
        spendLessSharedPreferencesDataSource.setPin(pin)
}
