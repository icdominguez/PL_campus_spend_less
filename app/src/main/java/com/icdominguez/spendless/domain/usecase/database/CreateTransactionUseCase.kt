package com.icdominguez.spendless.domain.usecase.database

import com.icdominguez.spendless.data.model.TransactionEntity
import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import com.icdominguez.spendless.model.RecurringFrequency
import java.time.LocalDateTime
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val localSpendLessRepository: LocalSpendLessRepository
) {
    suspend operator fun invoke(
        username: String,
        transceiver: String,
        category: String?,
        amount: Double,
        note: String,
        recurringFrequency: RecurringFrequency,
    ) {

        val nextRecurringDate = LocalDateTime.now().let {
            val nextDate = when(recurringFrequency) {
                RecurringFrequency.DAILY -> LocalDateTime.now().plusDays(1)
                RecurringFrequency.WEEKLY_ON -> LocalDateTime.now().plusWeeks(1)
                RecurringFrequency.MONTHLY_ON -> LocalDateTime.now().plusMonths(1)
                RecurringFrequency.YEARLY_ON -> LocalDateTime.now().plusYears(1)
                else -> null
            }

            nextDate?.withHour(0)?.withMinute(0)?.withSecond(0)?.withNano(0)
        }

        localSpendLessRepository.insertTransaction(
            TransactionEntity(
                username = username,
                category = category,
                amount = amount,
                note = note,
                date = LocalDateTime.now(),
                transceiver = transceiver,
                recurringFrequency = recurringFrequency.name,
                nextRecurringDate = nextRecurringDate
            )
        )
    }
}