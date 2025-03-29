package com.icdominguez.spendless.model

enum class ThousandSeparator(val symbol: String) {
    DOT("."),
    COMMA(","),
    SPACE(" ");

    fun getThousandSeparatorExample(): String =
        when (this) {
            DOT -> "1${DOT.symbol}00"
            COMMA -> "1${COMMA.symbol}00"
            SPACE -> "1${SPACE.symbol}000"
        }
}