package com.icdominguez.spendless.model

import android.content.Context
import com.icdominguez.spendless.R
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

enum class RecurringFrequency(val stringId: Int) {
    DOES_NOT_REPEAT(stringId = R.string.recurring_frequency_does_not_repeat),
    DAILY(stringId = R.string.recurring_frequency_daily),
    WEEKLY_ON(stringId = (R.string.recurring_frequency_weekly)),
    MONTHLY_ON(stringId = R.string.recurring_frequency_monthly),
    YEARLY_ON(stringId = R.string.recurring_frequency_yearly);
}

data class RecurringFrequencyItem(
    val name: String,
    val stringId: Int,
    val isSelected: Boolean = false,
    val dayValue: String,
)

fun getRecurringFrequencyItems(): List<RecurringFrequencyItem> {
    val locale = Locale.getDefault()

    return RecurringFrequency.entries.map {
        RecurringFrequencyItem(
            name = it.name,
            stringId = it.stringId,
            isSelected = it.name == RecurringFrequency.DOES_NOT_REPEAT.name,
            dayValue = when(it) {
              RecurringFrequency.WEEKLY_ON -> {
                  LocalDateTime.now().dayOfWeek.getDisplayName(TextStyle.FULL, locale)
              }
              RecurringFrequency.MONTHLY_ON -> {
                  if(locale.language == "en") {
                      getDaySuffix(LocalDateTime.now().dayOfMonth)
                  } else {
                      LocalDateTime.now().dayOfMonth.toString()
                  }
              }
              RecurringFrequency.YEARLY_ON -> {
                  if(locale.language == "en") {
                      getDaySuffix(LocalDateTime.now().dayOfMonth)
                  } else {
                      LocalDateTime.now().dayOfMonth.toString()
                  }
              }
              else -> ""
            }
        )
    }
}

private fun getDaySuffix(day: Int): String {
    return when {
        day in 11..13 -> "${day}th"
        day % 10 == 1 -> "${day}st"
        day % 10 == 2 -> "${day}nd"
        day % 10 == 3 -> "${day}rd"
        else -> "${day}th"
    }
}