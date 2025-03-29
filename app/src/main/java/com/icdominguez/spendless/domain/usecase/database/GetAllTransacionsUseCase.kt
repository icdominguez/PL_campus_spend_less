package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.data.model.toTransaction
import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.presentation.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTransactionsByUserUseCase @Inject constructor(
    private val localSpendLessRepository: LocalSpendLessRepository,
) {
    suspend operator fun invoke(
        username: String,
        expenseFormat: ExpenseFormat,
        thousandSeparator: String,
        decimalSeparator: String,
        currency: String,
    ): Flow<List<Transaction>> {
        return localSpendLessRepository.getAllTransactions(
            username = username
        ).map { list ->
            list.map {
                it.toTransaction(
                    expenseFormat = expenseFormat,
                    thousandSeparator = thousandSeparator,
                    decimalSeparator = decimalSeparator,
                    currency = currency,
                )
            }
        }
    }
}