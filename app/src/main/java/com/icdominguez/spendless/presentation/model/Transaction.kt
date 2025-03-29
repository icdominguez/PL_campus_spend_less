package com.icdominguez.spendless.presentation.model

import com.icdominguez.spendless.model.RecurringFrequency
import java.time.LocalDateTime

data class Transaction(
    val transceiver: String,
    val category: Category? = null,
    val amount: Double,
    val description: String = "",
    val date: LocalDateTime,
    val formatted: String,
    val recurringFrequency: RecurringFrequency? = null,
    val nextRecurringDate: LocalDateTime? = null,
)