package com.icdominguez.spendless.domain.usecase.preferences

import com.icdominguez.spendless.data.datasource.SpendLessSharedPreferencesDataSource
import javax.inject.Inject

class SetUsernameInSharedPreferencesUseCase @Inject constructor(
    private val spendLessSharedPreferencesDataSource: SpendLessSharedPreferencesDataSource
) {
    operator fun invoke(username: String) =
        spendLessSharedPreferencesDataSource.setUsername(username)
}
