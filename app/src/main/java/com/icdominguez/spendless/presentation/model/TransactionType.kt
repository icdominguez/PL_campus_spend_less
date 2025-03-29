package com.icdominguez.spendless.presentation.model

import com.icdominguez.spendless.R

enum class TransactionType(val icon: Int, val stringId: Int) {
    EXPENSE(stringId = R.string.expense, icon = R.drawable.trending_down_icon),
    INCOME(stringId = R.string.income, icon = R.drawable.trending_up_icon),
}

data class TransactionTypeItem (
    val name: String,
    val icon: Int,
    val stringId: Int,
    val isSelected: Boolean = false
)

fun getTransactionTypes(): List<TransactionTypeItem> =
    TransactionType.entries.map {
        TransactionTypeItem(
            name = it.name,
            icon = it.icon,
            stringId = it.stringId,
        )
    }