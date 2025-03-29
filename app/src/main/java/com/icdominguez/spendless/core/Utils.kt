package com.icdominguez.spendless.core

import android.annotation.SuppressLint
import android.content.Context
import com.icdominguez.spendless.R
import com.icdominguez.spendless.model.ExpenseFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val DAY_NAME_MONTH_NAME_MONTH_DAY_FORMAT = "MMMM d"
private const val MINUTES_SECONDS_FORMAT = "mm:ss"

fun LocalDate.toTodayYesterdayOrDate(context: Context) : String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when(this) {
        today -> context.getString(R.string.today)
        yesterday -> context.getString(R.string.yesterday)
        else -> {
            this.toDayNameMonthNameDayFormat()
        }
    }
}

fun LocalDate.toDayNameMonthNameDayFormat(): String {
    return DateTimeFormatter.ofPattern(DAY_NAME_MONTH_NAME_MONTH_DAY_FORMAT).format(this).replace(":", "-")
}

fun Double.formatTransaction(
    currency: String,
    thousandSeparator: String,
    decimalSeparator: String,
    expenseFormat: ExpenseFormat = ExpenseFormat.LESS,
    isExpense: Boolean = false,
): String {
    val formatter = DecimalFormat("#,###.00")
    formatter.decimalFormatSymbols = DecimalFormatSymbols().run {
        this.groupingSeparator = thousandSeparator.single()
        this.decimalSeparator = decimalSeparator.single()
        this
    }

    val formattedValue = "${currency}${formatter.format(this)}"

    return if(isExpense) {
        when(expenseFormat) {
            ExpenseFormat.LESS -> if (this < 0) "-${formattedValue.replace("-", "")}" else formattedValue
            ExpenseFormat.PARENTHESIS -> if (this < 0) "(${formattedValue.replace("-", "")})" else formattedValue
        }
    } else {
        formattedValue
    }
}

fun Long.millisToMinutesSecondsFormat(): String {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        ZoneId.systemDefault()
    )
    val formatter = DateTimeFormatter.ofPattern(
        MINUTES_SECONDS_FORMAT,
        Locale.getDefault()
    )

    return localDateTime.format(formatter)
}
