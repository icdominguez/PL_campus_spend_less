package com.icdominguez.spendless.model

enum class DecimalSeparator(val symbol: String,) {
    DOT("."),
    COMMA(",");

    fun getDecimalSeparatorExample(): String =
        when (this) {
            DOT -> "1${DOT.symbol}00"
            COMMA -> "1${COMMA.symbol}00"
        }
}