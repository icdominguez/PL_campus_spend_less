package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import javax.inject.Inject

class UpdateUserPreferencesUseCase @Inject constructor(
    private val localSpendLessRepository: LocalSpendLessRepository
) {
    suspend operator fun invoke(
        username: String,
        expenseFormat: String,
        currencySymbol: String,
        decimalSeparator: String,
        thousandSeparator: String,
    ) {
        localSpendLessRepository.updateUserPreferences(
            username = username,
            expenseFormat = expenseFormat,
            currencySymbol = currencySymbol,
            decimalSeparator = decimalSeparator,
            thousandSeparator = thousandSeparator,
        )
    }
}