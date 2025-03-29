package com.icdominguez.spendless.model

import com.icdominguez.spendless.R

enum class TransactionType(var stringId: Int) {
    EXPENSE(stringId = R.string.expense),
    INCOME(stringId = R.string.income),
}