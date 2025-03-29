package com.icdominguez.spendless.model

enum class ExpenseFormat {
    LESS,
    PARENTHESIS;

    fun getExpensesFormatsWithCurrency(currency: Currency): String =
        when (this) {
            LESS -> "-${currency.symbol}10"
            PARENTHESIS -> "(${currency.symbol}10)"
        }
}