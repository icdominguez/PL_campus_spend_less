package com.icdominguez.spendless.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.icdominguez.spendless.core.formatTransaction
import com.icdominguez.spendless.model.DecimalSeparator
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.model.RecurringFrequency
import com.icdominguez.spendless.model.ThousandSeparator
import com.icdominguez.spendless.presentation.model.Category
import com.icdominguez.spendless.presentation.model.Transaction
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["username"],
        childColumns = ["username"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,
    val transceiver: String,
    val username: String,
    val category: String? = null,
    val amount: Double,
    val note: String? = null,
    val date: LocalDateTime = LocalDateTime.now(),
    val recurringFrequency: String? = null,
    val nextRecurringDate: LocalDateTime? = null,
)

fun TransactionEntity.toTransaction(
    expenseFormat: ExpenseFormat,
    thousandSeparator: String,
    decimalSeparator: String,
    currency: String,
): Transaction {
    val currentCategory = Category.entries.firstOrNull { it.name == category }
    val formattedAmount = amount.formatTransaction(
        thousandSeparator = ThousandSeparator.entries.first { it.name == thousandSeparator }.symbol,
        decimalSeparator = DecimalSeparator.entries.first { it.name == decimalSeparator }.symbol,
        expenseFormat = ExpenseFormat.entries.first { it == expenseFormat },
        currency = currency,
        isExpense = currentCategory != null
    )
    val recurringFrequency = recurringFrequency?.let { RecurringFrequency.valueOf(it) }

    return Transaction(
        transceiver = transceiver,
        category = currentCategory,
        amount = amount,
        description = note ?: "",
        date = date,
        formatted = formattedAmount,
        recurringFrequency = recurringFrequency,
        nextRecurringDate = when(recurringFrequency) {
            RecurringFrequency.DAILY -> LocalDateTime.now().plusDays(1)
            RecurringFrequency.WEEKLY_ON -> LocalDateTime.now().plusWeeks(1)
            RecurringFrequency.MONTHLY_ON -> LocalDateTime.now().plusMonths(1)
            RecurringFrequency.YEARLY_ON -> LocalDateTime.now().plusYears(1)
            else -> null
        }
    )
}