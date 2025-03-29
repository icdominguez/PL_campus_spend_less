package com.icdominguez.spendless.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.icdominguez.spendless.R
import com.icdominguez.spendless.data.googletrusted.GoogleTrustedTimeManager
import com.icdominguez.spendless.data.model.toTransaction
import com.icdominguez.spendless.data.repository.LocalSpendLessRepository
import com.icdominguez.spendless.domain.usecase.database.GetUserLoggedInUseCase
import com.icdominguez.spendless.model.ExpenseFormat
import com.icdominguez.spendless.presentation.model.ExportOption
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

class ExportTransactionsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localSpendLessRepository: LocalSpendLessRepository,
    private val getUserLoggedInUseCase: GetUserLoggedInUseCase,
    private val googleTrustedTimeManager: GoogleTrustedTimeManager,
) {
    suspend operator fun invoke(
        exportOption: ExportOption
    ) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val userLoggedIn = getUserLoggedInUseCase()

        val googleMillis = googleTrustedTimeManager.getClient().computeCurrentUnixEpochMillis()

        val calendar = googleMillis?.let {
            Calendar.getInstance().apply {
                timeInMillis = it
            }
        } ?: Calendar.getInstance()

        val monthDateTime = when(exportOption) {
            ExportOption.ALL_DATA -> Pair(LocalDateTime.of(1970, 1, 1, 0, 0), LocalDateTime.now())
            ExportOption.CURRENT_MOTH -> {
                with(calendar) {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    val startOfCurrentMonth = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

                    add(Calendar.MONTH, 1)
                    set(Calendar.DAY_OF_MONTH, 1)
                    add(Calendar.MILLISECOND, -1)
                    val endOfCurrentMonth = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

                    Pair(startOfCurrentMonth, endOfCurrentMonth)
                }
            }
            ExportOption.LAST_THREE_MONTHS -> {
                calendar.add(Calendar.MONTH, -3)
                Pair(calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now())
            }
            ExportOption.LAST_MONTH -> {
                with(calendar) {
                    add(Calendar.MONTH, -1)
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    val startOfLastMonth = calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

                    add(Calendar.MONTH, 1)
                    set(Calendar.DAY_OF_MONTH, 1)
                    add(Calendar.MILLISECOND, -1)
                    val endOfLastMonth = calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

                    Pair(startOfLastMonth, endOfLastMonth)
                }
            }
        }

        userLoggedIn?.let { user ->
            val transactions = monthDateTime.let {
                localSpendLessRepository.getTransactionsFromDate(
                    username = userLoggedIn.username,
                    startDate = it.first,
                    endDate = it.second,
                ).map { transactionEntity ->
                    transactionEntity.toTransaction(
                        expenseFormat = ExpenseFormat.entries.first { it.name == user.expensesFormat },
                        decimalSeparator = user.decimalSeparator,
                        thousandSeparator = user.thousandSeparator,
                        currency = user.currency,
                    )
                }
            }

            val header = "${context.getString(R.string.transceiver)}, ${context.getString(R.string.category)}, ${context.getString(R.string.amount)}, ${context.getString(R.string.description)}, ${context.getString(R.string.date)}, ${context.getString(R.string.recurring_frequency)}, ${context.getString(R.string.recurring_frequency_next_recurring_date)}"
            val rows = transactions.joinToString("\n") { transaction ->
                listOf(
                    transaction.transceiver,
                    transaction.category?.let { context.getString(it.stringId) } ?: context.getString(R.string.income),
                    transaction.amount.toString(),
                    transaction.description,
                    transaction.date.format(formatter),
                    transaction.recurringFrequency,
                    transaction.nextRecurringDate,
                ).joinToString(",")
            }

            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "${context.getString(R.string.transactions)}_${context.getString(exportOption.stringId)}_${LocalDateTime.now().format(formatter)}.csv")
                put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            try {
                uri?.let { validUri ->
                    resolver.openOutputStream(validUri)?.use { it.write("$header\n$rows".toByteArray()) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}