package com.icdominguez.spendless.domain.usecase.database

import android.util.Log
import com.icdominguez.spendless.data.model.TransactionEntity
import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import com.icdominguez.spendless.model.RecurringFrequency
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class CheckRecurringTransactionsUseCase @Inject constructor(
    private val localSpendLessRepository: LocalSpendLessRepository,
) {
    suspend operator fun invoke(username: String) {
        val recurringFrequencyTransactions = localSpendLessRepository.getRecurringTransactions(username)

        recurringFrequencyTransactions.onEach { recurringFrequencyTransaction ->
            recurringFrequencyTransaction.nextRecurringDate?.let {

                val recurringFrequency = RecurringFrequency.valueOf(recurringFrequencyTransaction.recurringFrequency!!)

                if(recurringFrequencyTransaction.nextRecurringDate.toLocalDate() == LocalDate.now()) {
                    localSpendLessRepository.updateNextRecurringDateToNull(recurringFrequencyTransaction.transactionId)

                    localSpendLessRepository.insertTransaction(
                        TransactionEntity(
                            transceiver = recurringFrequencyTransaction.transceiver,
                            username = recurringFrequencyTransaction.username,
                            category = recurringFrequencyTransaction.category,
                            amount = recurringFrequencyTransaction.amount,
                            note = recurringFrequencyTransaction.note,
                            date = LocalDateTime.now(),
                            recurringFrequency = recurringFrequencyTransaction.recurringFrequency,
                            nextRecurringDate = when(recurringFrequency) {
                                RecurringFrequency.DAILY -> it.plusDays(1)
                                RecurringFrequency.WEEKLY_ON -> it.plusWeeks(1)
                                RecurringFrequency.MONTHLY_ON -> it.plusMonths(1)
                                RecurringFrequency.YEARLY_ON -> it.plusYears(1)
                                else -> null
                            }
                        )
                    )
                } else if(recurringFrequencyTransaction.nextRecurringDate.toLocalDate() < LocalDate.now()) {
                    localSpendLessRepository.updateNextRecurringDateToNull(recurringFrequencyTransaction.transactionId)

                    val transactionDate = recurringFrequencyTransaction.date.toLocalDate()
                    var lastDayUpdated = when(recurringFrequency) {
                        RecurringFrequency.DAILY -> transactionDate.plusDays(1)
                        RecurringFrequency.WEEKLY_ON -> transactionDate.plusWeeks(1)
                        RecurringFrequency.MONTHLY_ON -> transactionDate.plusMonths(1)
                        RecurringFrequency.YEARLY_ON -> transactionDate.plusYears(1)
                        else -> transactionDate
                    }

                    while(lastDayUpdated <= LocalDate.now()) {
                        val isLastIteration = lastDayUpdated == LocalDate.now()

                        val nextRecurringDate: LocalDateTime? = when(recurringFrequency) {
                            RecurringFrequency.DAILY -> lastDayUpdated.atTime(0,0,0).plusDays(1)
                            RecurringFrequency.WEEKLY_ON -> lastDayUpdated.atTime(0,0,0).plusWeeks(1)
                            RecurringFrequency.MONTHLY_ON -> lastDayUpdated.atTime(0,0,0).plusMonths(1)
                            RecurringFrequency.YEARLY_ON -> lastDayUpdated.atTime(0,0,0).plusYears(1)
                            else -> null
                        }

                        localSpendLessRepository.insertTransaction(
                            TransactionEntity(
                                transceiver = recurringFrequencyTransaction.transceiver,
                                username = recurringFrequencyTransaction.username,
                                category = recurringFrequencyTransaction.category,
                                amount = recurringFrequencyTransaction.amount,
                                note = recurringFrequencyTransaction.note,
                                date = lastDayUpdated.atTime(0,0,0),
                                recurringFrequency = if(isLastIteration) recurringFrequencyTransaction.recurringFrequency else null,
                                nextRecurringDate = if (isLastIteration) nextRecurringDate else null
                            )
                        )

                        lastDayUpdated = when(recurringFrequency) {
                            RecurringFrequency.DAILY -> lastDayUpdated.plusDays(1)
                            RecurringFrequency.WEEKLY_ON -> lastDayUpdated.plusWeeks(1)
                            RecurringFrequency.MONTHLY_ON -> lastDayUpdated.plusMonths(1)
                            RecurringFrequency.YEARLY_ON -> lastDayUpdated.plusYears(1)
                            else -> lastDayUpdated
                        }
                    }
                }
            }
        }
    }
}