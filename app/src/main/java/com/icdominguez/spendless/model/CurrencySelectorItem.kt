package com.icdominguez.spendless.model

enum class Currency(val symbol: String, val description: String) {
    USD("$", "US Dollar (USD)"),
    EUR("€", "Euro (EUR)"),
    POUND("£", "British Pound Sterling (GBP)"),
    YEN("¥", "Japanese Yen (JPY)"),
    SWISS("CHF", "Swiss Franc (CHF)"),
}